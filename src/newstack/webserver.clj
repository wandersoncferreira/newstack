(ns newstack.webserver
  (:require [compojure.core :as compojure :refer [GET]]
            [compojure.route :as route]
            [ring.middleware.params :as params]
            [manifold.deferred :as d]
            [com.stuartsierra.component :as component]
            [aleph.http :as http]))

(defn hello-world-handler [_]
  {:status 200
   :headers {"content-type" "text/plain"}
   :body "Prisionerss!!!"})

(defn delayed-get-prisioners-handler
  [req]
  (d/timeout!
   (d/deferred)
   1000
   (hello-world-handler req)))

(defn handler [database]
  (params/wrap-params
   (compojure/routes
    (GET "/prisioners" [] delayed-get-prisioners-handler)
    (route/not-found "You shall not pass!"))))

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
