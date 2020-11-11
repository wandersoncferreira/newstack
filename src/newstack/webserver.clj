(ns newstack.webserver
  (:require [compojure.core :as compojure :refer [GET POST]]
            [compojure.route :as route]
            [manifold.deferred :as d]
            [com.stuartsierra.component :as component]
            [aleph.http :as http]
            [clojure.data.json :as json]
            [newstack.entities :as entities]
            [schema.core :as s]))

(defn delayed-get-prisioners-handler
  [req]
  (d/timeout!
   (d/deferred)
   1000
   {:status 200
    :headers {"content-type" "text/plain"}
    :body "Prisionerss!!!"}))

(defn delayed-save-prisioners-handler
  [{:keys [body]}]
  (let [prisoner (json/read-str (slurp body) :key-fn keyword)]
    (when (s/validate entities/Prisoner prisoner)
      ;; try to register the prisoner
      )))

(defn delayed-save-king-handler
  [req])

(defmulti command :action)

(defmethod command :get-prisoners
  [req])

(defmethod command :default
  [req]
  {:status 404
   :headers {"content-type" "text/plain"}
   :body "We can't take that action now! The castle is a mess!\n"})

(defn command-handler
  [action-name]
  (fn [req]
    (command (merge {:action action-name} req))))

(defn handler [database]
  (compojure/routes
   (GET "/prisoners" [] (command-handler :get-prisoners))
   (POST "/prisoners" [] (command-handler :save-prisoners))
   (POST "/king" [] (command-handler :save-king))
   (GET "/king" [] (command-handler :list-king))
   (route/not-found "You shall not pass!")))

(defrecord Webserver [port database]
  component/Lifecycle
  (start [component]
    (let [srv (http/start-server (handler database) {:port port})]
      (assoc component :server srv)))
  (stop [component]
    (.close (:server component))
    (assoc component :server nil)))

(defn create-web-server [port]
  (component/using
   (map->Webserver {:port port})
   [:database]))
