(ns netty.world-server
  (:require [env :refer [mac?]]
            [netty :refer [channel-initializer channel-inbound-handler ->bytes ->str]]
            [clojure.tools.logging :as log])
  (:import (io.netty.bootstrap ServerBootstrap)
           (io.netty.buffer ByteBuf)
           (io.netty.channel.socket ServerSocketChannel)
           (io.netty.channel.epoll EpollEventLoopGroup EpollServerDomainSocketChannel)
           (io.netty.channel.kqueue KQueueEventLoopGroup KQueueServerDomainSocketChannel)
           (io.netty.channel.unix DomainSocketAddress)))

(defn start-server
  [path]
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

        address (DomainSocketAddress. path)

        group (if (mac?)
                (KQueueEventLoopGroup.)
                (EpollEventLoopGroup.))

        channel (if (mac?)
                  KQueueServerDomainSocketChannel
                  EpollServerDomainSocketChannel)

        b (doto (ServerBootstrap.)
            (.group group)
            (.channel channel)
            (.childHandler (channel-initializer handler)))

        ^ServerSocketChannel
        ch (-> b (.bind address) .sync .channel)]

    (-> ch .closeFuture .sync)))

(defn -main []
  (start-server "/tmp/app.world"))
