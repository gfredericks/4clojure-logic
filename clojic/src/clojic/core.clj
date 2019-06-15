(ns clojic.core
  (:gen-class)
  (:refer-clojure :exclude [==])
  (:require [clj-wrap-indent.core :as wrap]
            [clojic.problems :refer [problems]]
            [clojure.core.logic
             :refer [==
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

(defmacro defsolutions [nom & solpairs]
  `(def ~nom
     (->> (~@(partition 2 solpairs))
          quote
          (map vec)
          (into {}))))

(defsolutions solmap
  "Something trivial" :something
  "Something else trivial" '(1 2 3)
  ;; Your other solutions here:
  )

(defn- problem-description [description]
  (-> description
      (clojure.string/replace #"\s+" " ")
      (wrap/wrap-indent 60 3)))

(defn run-solutions []
  (let [stop (atom false)
        failing-test (atom nil)
        probcount (atom 0)]
    (doseq [{:keys [title description tests] :as p} problems
            :let [s (get solmap title :notdone)]
            :while (not @stop)]
      (swap! probcount inc)
      (println (apply str (repeat 60 "=")))
      (println (format "\n\nProblem %d:\n\t%s\n" @probcount title))
      (println (problem-description description))
      (doseq [t tests]
        (let [newcode (clojure.walk/postwalk-replace {'__ s} t)
              result (binding [*ns* (find-ns 'clojic.core)]
                       (eval newcode))]
          (println (format "\nTesting\n\n%s" (zp/zprint-str t 45) s))
          (if (or (not result)
                  (= s :notdone))
            (do
              (reset! stop true)
              (reset! failing-test title)
              (println (format "\n... with '__' set to '%s': %s"
                               s "FAIL")))
            (println (format "\n__  <-- %s : PASS!" s))))))
    (if-not @failing-test
      (println "\n\n\nAll tests pass!  You're a core logician!"))
    ;; For REPLing:
    [(if @stop :fail :pass) @failing-test]))

(defn -main [& _]
  (run-solutions)
  (shutdown-agents))

(comment
  (run-solutions))
