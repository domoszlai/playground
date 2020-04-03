(ns netty.world-server
  (:require [netty :refer [channel-initializer channel-inbound-handler ->bytebuf write-with-delimeter]]
            [netty.transport :as transport]
            [netty.handler :as hnd]
            [clojure.tools.logging :as log])
  (:import (io.netty.bootstrap ServerBootstrap)
           (io.netty.channel.socket ServerSocketChannel)
           (io.netty.util ReferenceCountUtil)))

(defn start-server
  [transport]
  (let [hellohnd (fn []
                   {:name "hello"
                    :handler (channel-inbound-handler

                              :channel-active
                              ([_ ctx]
                               (write-with-delimeter (.channel ctx) (->bytebuf "hello")))

                              :channel-inactive
                              ([_ ctx]
                               (log/info "Channel closed"))

                              :channel-read
                              ([_ ctx msg]
                               (log/info "Got a message" msg)
                               #_(ReferenceCountUtil/release msg)
                               (write-with-delimeter (.channel ctx) (->bytebuf "goodbye"))))})

        address (:address transport)
        group (:group transport)
        channel (:server-channel transport)

        b (doto (ServerBootstrap.)
            (.group group)
            (.channel channel)
            (.childHandler (channel-initializer [hnd/oexp
                                                 hnd/delimiter
                                                 hnd/string
                                                 hellohnd
                                                 hnd/iexp])))

        ^ServerSocketChannel
        ch (-> b (.bind address) .sync .channel)]

    (-> ch .closeFuture .sync)))

(defn -main []
  #_(start-server (transport/uds-transport "/tmp/app.world"))
  (start-server (transport/tcp-transport "127.0.0.1" 12000)))
