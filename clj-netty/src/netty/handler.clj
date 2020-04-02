(ns netty.handler
  (:require [netty :refer [channel-inbound-handler channel-outbound-handler]]
            [clojure.tools.logging :as log]))

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
