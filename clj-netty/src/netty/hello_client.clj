(ns netty.hello-client
  (:require [netty :refer [channel-initializer channel-inbound-handler ->bytebuf write-with-delimeter]]
            [netty.transport :as transport]
            [netty.handler :as hnd]
            [clojure.tools.logging :as log])
  (:import (io.netty.bootstrap Bootstrap)
           (io.netty.channel Channel)
           (io.netty.util ReferenceCountUtil)))

(defn start-client
  [transport]
  (let [hellohnd (fn []
                   {:name "hello"
                    :handler  (channel-inbound-handler

                               :channel-active
                               ([_ ctx]
                                (write-with-delimeter (.channel ctx) (->bytebuf "hello")))

                               :channel-inactive
                               ([_ ctx]
                                (log/info "Channel closed"))

                               :channel-read
                               ([_ ctx msg]
                                (log/info "Got a message" msg)
                                #_(ReferenceCountUtil/release msg)))})

        address (:address transport)
        group (:group transport)
        channel (:client-channel transport)

        b (doto (Bootstrap.)
            (.group group)
            (.channel channel)
            (.handler (channel-initializer [hnd/oexp
                                            hnd/delimiter
                                            hnd/string
                                            hellohnd
                                            hnd/iexp])))

        ^Channel
        ch (-> b (.connect address) .sync .channel)]

    (-> ch .closeFuture .sync)))

(defn -main []
  #_(start-client (transport/uds-transport "/tmp/app.world"))
  (start-client (transport/tcp-transport "127.0.0.1" 12000)))
