(ns newstack.entities
  (:require [schema.core :as s]
            [schema-generators.generators :as g]))

(def Prisoner
  {:prisoner/name s/Str
   (s/optional-key :prisoner/age) s/Int
   (s/optional-key :prisoner/religion) #{s/Keyword}
   (s/optional-key :prisoner/sentence-years) s/Int})

(def King
  {:king/name s/Str
   :king/ruling? s/Bool
   :king/family-origin #{s/Keyword}})

(def Bridge
  {:bridge/weight s/Int
   :bridge/width s/Int
   :bridge/height s/Int
   :bridge/place #{s/Keyword}})

(def Cannon
  {:cannon/loaded? s/Bool
   :cannon/reach s/Num})

(def Castle
  {:castle/cannon Cannon
   :castle/bridge Bridge
   :castle/king King
   :castle/prisoners [Prisoner]})

(defn ->random-castle []
  (g/generate Castle))
