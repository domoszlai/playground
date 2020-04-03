(ns netty.handler
  (:require [netty :refer [channel-inbound-handler channel-outbound-handler]]
            [clojure.tools.logging :as log])
  (:import (io.netty.handler.codec DelimiterBasedFrameDecoder Delimiters MessageToMessageDecoder)
           (io.netty.handler.codec.string StringDecoder)
           (io.netty.buffer ByteBuf ByteBufUtil)))

(defn iexp
  []
  {:name "iexp"
   :handler (channel-inbound-handler

             :exception-caught
             ([_ ctx cause]
              (log/error (.getMessage cause))
              (.close ctx)))})

(defn oexp
  []
  {:name "oexp"
   :handler (channel-outbound-handler

             :exception-caught
             ([_ ctx cause]
              (log/error (.getMessage cause))
              (.close ctx)))})

(defn debug-read
  [dbgmsg]
  {:name (str "debug-" dbgmsg)
   :handler (channel-inbound-handler

             :channel-read
             ([_ ctx msg]
              (if (instance? ByteBuf msg)
                (log/info dbgmsg (ByteBufUtil/hexDump (cast ByteBuf msg)))
                (log/info dbgmsg msg))
              (.fireChannelRead ctx msg)))})

(def max-frame-size
  1024)

(defn delimiter
  []
  {:name "delimiter"
   :handler (DelimiterBasedFrameDecoder. max-frame-size (Delimiters/lineDelimiter))})

(defn string
  []
  {:name "string"
   :handler (StringDecoder.)})
