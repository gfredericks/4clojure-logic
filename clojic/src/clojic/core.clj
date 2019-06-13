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

(def my-solutions
  {"Something trivial" :something
   "Something else trivial" '(1 2 3)
   ;; Fill in the rest of your solutions here!
   })

(defn- problem-description [description]
  (-> description
      (clojure.string/replace #"\s+" " ")
      (wrap/wrap-indent
       60 3)))

(defn run-solutions []
  (let [stop (atom false)
        failing-test (atom nil)
        probcount (atom 0)]
    (doseq [{:keys [title description tests] :as p} problems
            :let [s (get my-solutions title :notdone)]
            :while (not @stop)]
      (swap! probcount inc)
      (println (format "\n\nProblem %d:\n\t%s\n" @probcount title))
      (println (problem-description description))
      (doseq [t tests]
        (let [newcode (clojure.walk/postwalk-replace
                       {'__ (list 'quote s)} t)
              evaluated (binding [*ns* (find-ns 'clojic.core)]
                          (eval newcode))
              result (true? evaluated)]
          (println (format "\nTesting\n%s" (zp/zprint-str t 45) s))
          (if-not (or (not result)
                      (= s :notdone))
            (println (format "\n__  == %s : PASS!" s))
            (do
              (reset! stop true)
              (reset! failing-test title)
              (println (format "\nTesting with __ set to '%s': %s"
                               s (if result "pass!!" "FAIL"))))))))
    [(if @stop :fail :pass) @failing-test]))

(defn -main [& _]
  (run-solutions))

(comment
  (run-solutions))
