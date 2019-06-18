(ns clojic.core
  (:gen-class)
  (:refer-clojure :exclude [==])
  (:require [clj-wrap-indent.core :as wrap]
            [clojic.problems :refer [problems]]
            [clojic.solutions :as sol]
            [clojure.core.logic
             :refer [==
                     !=
                     conde
                     conso
                     fail
                     firsto
                     fresh
                     matche
                     resto
                     run
                     run*
                     succeed]]
            [zprint.core :as zp]))

;; !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
;; Enter your solutions in solutions.clj!!!!
;; !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

(defn- problem-description [description]
  (-> description
      (clojure.string/replace #"\s+" " ")
      (wrap/wrap-indent 60 3)))

(defn evaluate
  "
  Evaluate supplied expression, logging exceptions without stacktraces
  to aid readability while progressing through the problem sequence.
  "
  [expr]
  (binding [*ns* (find-ns 'clojic.core)]
    (try
      (eval expr)
      (catch Throwable t
        (println "\nException!!!")
        (println (.getMessage t))
        (println "!!!!!!!!!!!!")
        :exception))))

(defn run-solutions []
  (let [progress (atom {:stop false
                        :failing-test nil
                        :probcount 0})]
    (doseq [{:keys [title description tests] :as p} problems
            :let [s (get sol/solmap title :notdone)]
            :while (not (get @progress :stop))]
      (swap! progress update :probcount inc)
      (println (apply str (repeat 60 "=")))
      (println (format "\n\nProblem %d:\n\t%s\n" (:probcount @progress) title))
      (println (problem-description description))
      (doseq [t tests]
        (let [newcode (clojure.walk/postwalk-replace {'__ s} t)
              result (evaluate newcode)]
          (println (format "\nTesting\n\n%s" (zp/zprint-str t 45) s))
          (if (or (not result)
                  (= s :notdone)
                  (= result :exception))
            (do
              (swap! progress
                     assoc :stop true, :failing-test title)
              (println (format "\n... with '__' set to '%s': %s"
                               s "FAIL")))
            (println (format "\n__  <-- %s : PASS!" s))))))
    (if (:stop @progress)
      ;; For REPLing, return where tests are currently failing:
      (:failing-test @progress)
      (println "\n\n\nAll tests pass!  You're a core logician!"))))

(defn -main [& _]
  (run-solutions)
  (shutdown-agents))
