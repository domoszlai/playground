(ns netty
  (:import (io.netty.channel Channel ChannelInitializer ChannelInboundHandler ChannelOutboundHandler ChannelHandler)
           (io.netty.buffer ByteBuf Unpooled)
           (java.nio.charset Charset)))

(def utf8
  (Charset/forName "UTF-8"))

(defn ->bytebuf
  [str]
  (Unpooled/copiedBuffer str utf8))

(defn ->str
  [bytebuf]
  (.toString (cast ByteBuf bytebuf) utf8))

(defn channel-initializer [handlers]
  (proxy [ChannelInitializer] []
    (initChannel [^Channel ch]
      (doseq [{:keys [name handler]} handlers]
        (.addLast (.pipeline ch) name handler)))))

(defmacro channel-inbound-handler
  [& {:as handlers}]
  `(reify
     ChannelHandler
     ChannelInboundHandler

     (handlerAdded
       ~@(or (:handler-added handlers) `([_# _#])))
     (handlerRemoved
       ~@(or (:handler-removed handlers) `([_# _#])))
     (exceptionCaught
       ~@(or (:exception-caught handlers)
             `([_# ctx# cause#]
               (.fireExceptionCaught ctx# cause#))))
     (channelRegistered
       ~@(or (:channel-registered handlers)
             `([_# ctx#]
               (.fireChannelRegistered ctx#))))
     (channelUnregistered
       ~@(or (:channel-unregistered handlers)
             `([_# ctx#]
               (.fireChannelUnregistered ctx#))))
     (channelActive
       ~@(or (:channel-active handlers)
             `([_# ctx#]
               (.fireChannelActive ctx#))))
     (channelInactive
       ~@(or (:channel-inactive handlers)
             `([_# ctx#]
               (.fireChannelInactive ctx#))))
     (channelRead
       ~@(or (:channel-read handlers)
             `([_# ctx# msg#]
               (.fireChannelRead ctx# msg#))))
     (channelReadComplete
       ~@(or (:channel-read-complete handlers)
             `([_# ctx#]
               (.fireChannelReadComplete ctx#))))
     (userEventTriggered
       ~@(or (:user-event-triggered handlers)
             `([_# ctx# evt#]
               (.fireUserEventTriggered ctx# evt#))))
     (channelWritabilityChanged
       ~@(or (:channel-writability-changed handlers)
             `([_# ctx#]
               (.fireChannelWritabilityChanged ctx#))))))

(defmacro channel-outbound-handler
  [& {:as handlers}]
  `(reify
     ChannelHandler
     ChannelOutboundHandler

     (handlerAdded
       ~@(or (:handler-added handlers) `([_# _#])))
     (handlerRemoved
       ~@(or (:handler-removed handlers) `([_# _#])))
     (exceptionCaught
       ~@(or (:exception-caught handlers)
             `([_# ctx# cause#]
               (.fireExceptionCaught ctx# cause#))))
     (bind
       ~@(or (:bind handlers)
             `([_# ctx# local-address# promise#]
               (.bind ctx# local-address# promise#))))
     (connect
       ~@(or (:connect handlers)
             `([_# ctx# remote-address# local-address# promise#]
               (.connect ctx# remote-address# local-address# promise#))))
     (disconnect
       ~@(or (:disconnect handlers)
             `([_# ctx# promise#]
               (.disconnect ctx# promise#))))
     (close
       ~@(or (:close handlers)
             `([_# ctx# promise#]
               (.close ctx# promise#))))
     (read
       ~@(or (:read handlers)
             `([_# ctx#]
               (.read ctx#))))
     (write
       ~@(or (:write handlers)
             `([_# ctx# msg# promise#]
               (.write ctx# msg# promise#))))
     (flush
       ~@(or (:flush handlers)
             `([_# ctx#]
               (.flush ctx#))))))
