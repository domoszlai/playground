(ns netty.world-server
  (:require [netty :refer [channel-initializer channel-inbound-handler ->bytes ->str]]
            [netty.transport :as transport]
            [clojure.tools.logging :as log])
  (:import (io.netty.bootstrap ServerBootstrap)
           (io.netty.buffer ByteBuf)
           (io.netty.channel.socket ServerSocketChannel)))

(defn start-server
  [transport]
  (let [handler (channel-inbound-handler

                 :channel-active
                 ([_ ctx]
                  (let [buff (.buffer (.alloc ctx))]
                    (.writeBytes buff (->bytes "hello"))
                    (.writeAndFlush ctx buff)))

                 :channel-read
                 ([_ ctx msg]
                  (let [str (->str (cast ByteBuf msg))
                        buff (.buffer (.alloc ctx))]
                    (log/info "Got a message" str)
                    (.writeBytes buff (->bytes "goodbye"))
                    (.writeAndFlush ctx buff))))

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
