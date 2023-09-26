(ns sk.handlers.home.model
  (:require [sk.models.crud :refer [Query db]]))

(defn get-rows []
  (Query db ["select * from asistencia order by id"]))

(defn ventas-rows []
  (Query db ["select * from ventas order by id"]))

(defn somos-rows []
  (Query db ["select * from mision order by id limit 1"]))

(comment
  (somos-rows)
  (ventas-rows)
  (get-rows))
