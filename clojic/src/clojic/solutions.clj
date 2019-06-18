(ns clojic.solutions
  (:require [clojic.macros :refer [defsolutions]]))

(defsolutions solmap
  "First problem" :fails     ;; should be: :something
  "Moar triviality" :fails   ;; should be: '(1 2 3)
  ;; Your other solutions here ...:
  )

