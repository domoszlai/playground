(ns netty.hello-client
  (:require [env :refer [mac?]]
            [netty :refer [channel-initializer channel-inbound-handler ->bytes ->str]]
            [clojure.tools.logging :as log])
  (:import (io.netty.bootstrap Bootstrap)
           (io.netty.buffer ByteBuf)
           (io.netty.channel Channel)
           (io.netty.channel.epoll EpollEventLoopGroup EpollDomainSocketChannel)
           (io.netty.channel.kqueue KQueueEventLoopGroup KQueueDomainSocketChannel)
           (io.netty.channel.unix DomainSocketAddress)))

;; https://stackoverflow.com/questions/33296749/netty-connect-to-unix-domain-socket-failed

(defn start-client
  [path]
  (let [handler (channel-inbound-handler

                 :channel-active
                 ([_ ctx]
                  (let [buff (.buffer (.alloc ctx))]
                    (.writeBytes buff (->bytes "hello"))
                    (.writeAndFlush ctx buff)))

                 :channel-read
                 ([_ ctx msg]
                  (let [str (->str (cast ByteBuf msg))]
                    (log/info "Got a message" str))))

        address (DomainSocketAddress. path)

        group (if (mac?)
                (KQueueEventLoopGroup.)
                (EpollEventLoopGroup.))

        channel (if (mac?)
                  KQueueDomainSocketChannel
                  EpollDomainSocketChannel)

        b (doto (Bootstrap.)
            (.group group)
            (.channel channel)
            (.handler (channel-initializer handler)))

        ^Channel
        ch (-> b (.connect address) .sync .channel)]

    (-> ch .closeFuture .sync)))

(defn -main []
  (start-client "/tmp/app.world"))
