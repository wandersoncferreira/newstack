(ns newstack.system
  (:require [com.stuartsierra.component :as component]
            [newstack.webserver :as web]
            [newstack.database :as db]))

(def ^:private web-port
  "You should not be here, right? Maybe an env variable."
  4006)

(defn sys []
  (component/system-map
   :database (db/create-new-mongodb)
   :app (web/create-web-server web-port)))

(defn main []
  (component/start (sys)))
