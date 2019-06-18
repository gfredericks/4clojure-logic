(ns clojic.solutions
  (:require [clojic.macros :refer [defsolutions]]))

(defsolutions solmap
  ;; Problem Name ...  Solution ...
  "First problem"      :fails    ;; should be :something
  "Moar triviality"    :fails    ;; should be '(1 2 3)
  ;; Your other solutions here ...:
  )

;; For REPL-driving your workflow, which is substantially faster,
;; uncomment this form and evaluate the file every time you change
;; your solutions.
#_(do
    (require '[clojic.core])
    (clojic.core/run-solutions))
