(ns netty.hello-client
  (:require [netty :refer [channel-initializer channel-inbound-handler ->bytebuf ->str]]
            [netty.transport :as transport]
            [netty.handler :as hnd]
            [clojure.tools.logging :as log])
  (:import (io.netty.bootstrap Bootstrap)
           (io.netty.channel Channel)
           (io.netty.util ReferenceCountUtil)))

(defn start-client
  [transport]
  (let [hellohnd
        {:name "hello"
         :handler  (channel-inbound-handler

                    :channel-active
                    ([_ ctx]
                     (.writeAndFlush (.channel ctx) (->bytebuf "hello")))

                    :channel-read
                    ([_ ctx msg]
                     (log/info "Got a message" (->str msg))
                     (ReferenceCountUtil/release msg)))}

        address (:address transport)
        group (:group transport)
        channel (:client-channel transport)

        b (doto (Bootstrap.)
            (.group group)
            (.channel channel)
            (.handler (channel-initializer [(hnd/oexp) hellohnd (hnd/iexp)])))

        ^Channel
        ch (-> b (.connect address) .sync .channel)]

    (-> ch .closeFuture .sync)))

(defn -main []
  #_(start-client (transport/uds-transport "/tmp/app.world"))
  (start-client (transport/tcp-transport "127.0.0.1" 12000)))
