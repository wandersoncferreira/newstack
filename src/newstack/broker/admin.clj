(ns newstack.broker.admin
  (:require [dvlopt.kafka.admin :as k.admin]
            [dvlopt.kafka :as k]
            [dvlopt.kafka.out :as k.out]))

(def ^:private kafka-nodes [["kafka1" 19091]
                            ["kafka2" 19092]
                            ["kafka3" 19093]])

(defn create-topic
  [topic-name partition-number replica-factor]
  (try
    (with-open [admin (k.admin/admin {::k/nodes kafka-nodes})]
      (let [res (k.admin/create-topics admin {topic-name {::k.admin/number-of-partitions partition-number
                                                          ::k.admin/replication-factor replica-factor
                                                          ::k.admin/configuration {"cleanup.policy" "compact"}}})]
        @(get res topic-name)))
    (catch java.util.concurrent.ExecutionException e
      (let [error-map (Throwable->map e)
            inner-error (second (:via error-map))]
        (case (:type inner-error)
          org.apache.kafka.common.errors.TopicExistsException 
          {:status :error
           :reason (format "Topic %s already exists" topic-name)}

          {:status :error
           :reason "No rationale available"})))))

(defn produce-records
  [producer-id topic-name key->value-fn key-serializer value-serializer]
  (with-open [producer (k.out/producer {::k/nodes kafka-nodes
                                        ::k/serializer.key key-serializer
                                        ::k/serializer.value value-serializer
                                        ::k.out/configuration {"client.id" producer-id}})]
    (dorun
     (map
      (fn [[k v]]
        (k.out/send producer {::k/topic topic-name
                              ::k/key k
                              ::k/value v}
                    (fn callback [e m]
                      (println "Record %s : %s" k (if e "failure" "success")))))
      (key->value-fn)))))
