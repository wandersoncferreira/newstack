(ns newstack.database
  (:require [monger.core :as mg]
            [com.stuartsierra.component :as component]))

(def ^:private mongo-uri
  "You should not be here, right? Maybe an ENV variable."
  "mongodb://regular:senha@db:27017/newstack")

(defrecord MongoDB []
  component/Lifecycle
  (start [component]
    (let [obj (mg/connect-via-uri mongo-uri)]
      (-> component
          (assoc :conn (:conn obj))
          (assoc :db (:db obj)))))
  (stop [component]
    (let [{:keys [conn]} component]
      (mg/disconnect conn)
      (assoc component :conn nil))))

(defn create-new-mongodb []
  (->MongoDB))

(defn save-prisoner []
  ;; 1. verify if castle exist
  ;; 2. we can only have prisoners if there is a king in power
  )
