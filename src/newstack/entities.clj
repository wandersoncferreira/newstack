(ns newstack.entities
  (:require [schema.core :as s]))

(def Prisoner
  {:prisoner/name s/Str
   :prisoner/age s/Num
   :prisoner/religion #{:sci-fi :cloudy :hopeful}
   :prisoner/sentence-years s/Num})

(def King
  {:king/name s/Str
   :king/ruling? s/Bool
   :king/family-origin #{:royal :regular-guy}})

(def Bridge
  {:bridge/weight s/Num
   :bridge/width s/Num
   :bridge/height s/Num
   :bridge/place #{:north :south :east :west}})

(def Cannon
  {:cannon/loaded? s/Bool
   :cannon/reach s/Num})

(def Castle
  {:castle/cannon Cannon
   :castle/bridge Bridge
   :castle/king King
   :castle/prisoners [Prisoner]})
