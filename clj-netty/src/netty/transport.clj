(ns netty.transport
  (:require [env :refer [mac?]])
  (:import (io.netty.channel.epoll EpollEventLoopGroup EpollDomainSocketChannel EpollServerDomainSocketChannel)
           (io.netty.channel.kqueue KQueueEventLoopGroup KQueueDomainSocketChannel KQueueServerDomainSocketChannel)
           (io.netty.channel.unix DomainSocketAddress)
           (io.netty.channel.socket.nio NioSocketChannel NioServerSocketChannel)
           (io.netty.channel.nio NioEventLoopGroup)
           (java.net InetSocketAddress)))

(defn uds-transport
  [path]
  {:address (DomainSocketAddress. path)
   :group (if (mac?)
            (KQueueEventLoopGroup.)
            (EpollEventLoopGroup.))
   :client-channel (if (mac?)
                     KQueueDomainSocketChannel
                     EpollDomainSocketChannel)
   :server-channel (if (mac?)
                     KQueueServerDomainSocketChannel
                     EpollServerDomainSocketChannel)})

(defn tcp-transport
  [host port]
  {:address (InetSocketAddress. host port)
   :group (NioEventLoopGroup.)
   :client-channel NioSocketChannel
   :server-channel NioServerSocketChannel})
