(ns clojic.core
  (:gen-class)
  (:refer-clojure :exclude [==])
  (:require [clj-wrap-indent.core :as wrap]
            [clojic.problems :refer [problems]]
            [clojure.core.logic
             :refer [run* fail succeed == fresh conde]]))

(def my-solutions
  {"Something trivial" :something
   "Something else trivial" '(1 2 3)
   ;; Fill in the rest of your solutions here!
   })

(defn- problem-title [title probcount]
  (format "\n\nProblem %d:\n\t%s\n" probcount title))

(defn- problem-description
  [description]
  (wrap/wrap-indent (clojure.string/replace description #"\s+" " ")
                    60 3))

(defn run-solutions []
  (let [stop (atom false)
        probcount (atom 0)]
    (doseq [{:keys [title description tests] :as p} problems
            :let [s (get my-solutions title :notdone)]
            :while (not @stop)]
      (swap! probcount inc)
      (println (problem-title title @probcount))
      (println (problem-description description))
      (doseq [t tests]
        (let [newcode (clojure.walk/postwalk-replace
                       {'__ (list 'quote s)} t)
              evaluated (binding [*ns* (find-ns 'clojic.core)]
                          (eval newcode))
              result (if (true? evaluated) "pass!!" "FAIL")]
          (when (or (not result)
                    (= s :notdone))
            (reset! stop true)
            (println (format "\nTesting %s\nwith __ set to '%s': %s"
                             t
                             s
                             result))))))))

(defn -main [& _]
  (run-solutions))

(run-solutions)
