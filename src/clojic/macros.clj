(ns clojic.macros)

(defmacro defsolutions [nom & solpairs]
  `(def ~nom
     (->> (~@(partition 2 solpairs))
          quote
          (map vec)
          (into {}))))
