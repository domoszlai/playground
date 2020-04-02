(ns netty.world-server
  (:require [netty :refer [channel-initializer channel-inbound-handler ->bytebuf ->str]]
            [netty.transport :as transport]
            [clojure.tools.logging :as log])
  (:import (io.netty.bootstrap ServerBootstrap)
           (io.netty.channel.socket ServerSocketChannel)
           (io.netty.util ReferenceCountUtil)))

(defn start-server
  [transport]
  (let [handler (channel-inbound-handler

                 :channel-active
                 ([_ ctx]
                  (.writeAndFlush (.channel ctx) (->bytebuf "hello")))

                 :channel-read
                 ([_ ctx msg]
                  (log/info "Got a message" (->str msg))
                  (ReferenceCountUtil/release msg)
                  (.writeAndFlush (.channel ctx) (->bytebuf "goodbye"))))

        address (:address transport)
        group (:group transport)
        channel (:server-channel transport)

        b (doto (ServerBootstrap.)
            (.group group)
            (.channel channel)
            (.childHandler (channel-initializer handler)))

        ^ServerSocketChannel
        ch (-> b (.bind address) .sync .channel)]

    (-> ch .closeFuture .sync)))

(defn -main []
  #_(start-server (transport/uds-transport "/tmp/app.world"))
  (start-server (transport/tcp-transport "127.0.0.1" 12000)))
