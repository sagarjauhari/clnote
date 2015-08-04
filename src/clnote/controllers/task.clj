(ns clnote.controllers.task
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]))

(defn validate-task [params]
  (first
    (b/validate
      params
      :title [v/required [v/min-count 3]]
      :completed [v/required v/boolean]
      :rank [v/required v/number v/positive])))

(defn group-tasks-by-rank [tasks]
  (group-by #(:rank %) tasks))
