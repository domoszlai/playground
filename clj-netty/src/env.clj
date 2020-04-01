(ns env
  (:require [clojure.string]))

(defn get-system-properties
  "Get System Properties"
  []
  (let [sys-properties (System/getProperties)
        properties-i (.iterator (.keySet sys-properties))]
    (if (.hasNext properties-i)
      (loop [k (.next properties-i) properties {}]
        (if (not (.hasNext properties-i))
          (assoc properties (keyword k) (System/getProperty k))
          (recur (.next properties-i)
                 (assoc properties
                        (keyword k)
                        (System/getProperty k)))))
      {})))

(defn mac?
  "Is Macintosh?"
  []
  (let [properties (get-system-properties)]
    (if (and (:os.name properties)
             (re-matches #"mac.*"
                         (clojure.string/lower-case (:os.name (get-system-properties)))))
      true
      false)))
