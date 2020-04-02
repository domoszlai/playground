(ns netty.hello-client
  (:require [netty :refer [channel-initializer channel-inbound-handler ->bytes ->str]]
            [netty.transport :as transport]
            [clojure.tools.logging :as log])
  (:import (io.netty.bootstrap Bootstrap)
           (io.netty.buffer ByteBuf)
           (io.netty.channel Channel)
           (io.netty.util ReferenceCountUtil)))

(defn start-client
  [transport]
  (let [handler (channel-inbound-handler

                 :channel-active
                 ([_ ctx]
                  (let [buff (.buffer (.alloc ctx))]
                    (.writeBytes buff (->bytes "hello"))
                    (.writeAndFlush ctx buff)))

                 :channel-read
                 ([_ ctx msg]
                  (let [str (->str (cast ByteBuf msg))]
                    (log/info "Got a message" str))
                  (ReferenceCountUtil/release msg)))

        address (:address transport)
        group (:group transport)
        channel (:client-channel transport)

        b (doto (Bootstrap.)
            (.group group)
            (.channel channel)
            (.handler (channel-initializer handler)))

        ^Channel
        ch (-> b (.connect address) .sync .channel)]

    (-> ch .closeFuture .sync)))

(defn -main []
  #_(start-client (transport/uds-transport "/tmp/app.world"))
  (start-client (transport/tcp-transport "127.0.0.1" 12000)))
