(ns clojic.problems)

(def problems
  [;; The first two are just to get people started:
   {:title "First problem"
    :description "Something trivial, to get you started. Edit solutions.clj
                  and set the first solution to \":something\" (without the
                  quotes)."
    :tests '[(= __ :something)]}
   {:title "Moar triviality"
    :description "Put '(1 2 3) here"
    :tests '[(= __ '(1 2 3))]}
   ;; Real core.logic tests start here.
   {:title "Failure"
    :description "Logic program is all about goals. The 'fail' goal
                  never succeeds. run* always returns a (possibly empty)
                  list of values for q. In this case, no goal succeeds,
                  so there will be no values for q."
    :tests '[(= __ (run* [q] fail))]
    :tags ["TRS"]}
   {:title "Unification 1"
    :description "Unification succeeds when the two terms can be made equal.
                  When the following unification succeeds, \"q\" is associated
                  with the other term."
    :tests '[(= __ (run* [q] (== :mark q)))]
    :tags ["GF"]}
   {:title "Unification 2"
    :description "If the only goal is unification, and that fails, \"q\"
                  is not associated with any values."
    :tests '[(= __ (run* [q] (== 77 15)))]
    :tags ["GF"]}
   {:title "Unification 3"
    :description "Given multiple, contratictory unification goals, no values
                  for \"q\" are generated."
    :tests '[(= __ (run* [q] (== q 97) (== q 98)))]
    :tags ["GF"]}
   {:title "Unification 4"
    :description "The truth, the whole truth, and nothing but the truth..."
    :tests '[(= __ (run* [q] succeed (== true q)))]
    :tags ["TRS"]}
   {:title "Unification 5"
    :description "This is a somewhat corny, similar version"
    :tests '[(= __ (run* [r] succeed (== :corn r)))]
    :tags ["TRS"]}
   {:title "A Fresh Success"
    :description "The succeed goal always succeeds; a succeeding value
                  not bound to anything in particular is called a 'fresh' value, and
                  is one of _0, _1, _2, ...."
    :tests '[(= __ (run* [q] succeed))]
    :tags ["TRS"]}
   {:title "Lettings 1"
    :description "You can (somewhat) mix logic programming
                  with functional programming."
    :tests '[(= __ (run* [q] (let [x true] (== false x))))]
    :tags ["TRS"]}
   {:title "Lettings 2"
    :description "Let works the way you (hopefully) would expect."
    :tests '[(= __ (run* [q] (let [x false] (== false x))))]
    :tags ["TRS"]}
   {:title "Freshings 1"
    :description "Fresh introduces new logic variables."
    :tests '[(= __ (run* [q] (fresh [x] (== true x) (== true q))))]
    :tags ["TRS"]}
   {:title "Freshings 2"
    :description "What happens if we unify two logic variables?"
    :tests '[(= __ (run* [q] (fresh [x] (== x q) (== :snazzy x))))]
    :tags ["GF"]}
   {:title "Scopings"
    :description "Oh man which x is which? What does it all mean??"
    :tests '[(= __ (run* [x] (let [x true] (fresh [x] (== false x)))))]
    :tags ["TRS"]}
   {:title "List-unification"
    :description "Core.logic supports unifying sequential
  things. Vectors and seqs are treated the same."
    :tests '[(= __ (run* [q] (fresh [x y] (== [x y] q))))]
    :tags ["TRS"]}
   {:title "Multiple Query Parameters"
    :description "When we use multiple query parameters, run returns a
                list of tuples instead of a list of single values."
    :tests '[(= __ (run* [a b c] (== a c) (== c :foo) (== b 42)))
             (= __ (run* [q]
                     (fresh [a b c]
                       (== q [a b c])
                       (== a c)
                       (== c :foo)
                       (== b 42))))]
    :tags ["GF"]}
   {:title "Oh man what is this doing"
    :description "It's all nonsense! Who would write code like this?"
    :tests '[(= __ (run* [r]
                     (fresh [x]
                       (let [y x]
                         (fresh [x]
                           (== [y x y] r))))))]
    :tags ["TRS"]}
   {:title "Unify twice! Unify twice!"
    :description "What happens if we try to unify a logic variable to
                  two different values?"
    :tests '[(= __ (run* [q] (== false q) (== true q)))]
    :tags ["TRS"]}
   {:title "Unify twice again! Unify twice again!"
    :description "What happens if we try to unify a logic variable to
                  the same value twice?"
    :tests '[(= __ (run* [q] (== false q) (== false q)))]
    :tags ["TRS"]}
   {:title "Run! Let!"
    :description "Can a regular clojure local refer to a logic variable?"
    :tests '[(= __ (run* [q] (let [x q] (== true x))))]
    :tags ["TRS"]}
   {:title "Run! Fresh!"
    :description "What happens if we unify two logic variables when
                  neither is ground?"
    :tests '[(= __ (run* [q] (fresh [x] (== x q))))]
    :tags ["TRS"]}
   {:title "Run! Fresh! Two!"
    :description "What if we unify q with x and then give x a value \"later\"?"
    :tests '[(= __ (run* [q] (fresh [x] (== x q) (== true x))))]
    :tags ["TRS"]}
   {:title "Conde 1"
    :description "Conde lets us give independent alternatives. The top
                  level clauses (in square brackets) can succeed or
                  fail indepedently. Conde is the primary mechanism by
                  which a logic program can return multiple
                  values. The clauses inside the square brackets must
                  evaluate to goals, and are combined with conjunction
                  (i.e., \"and\")."
    :tests '[(= __ (run* [q]
                     (conde
                      [(== q :foo)]
                      [(== :bar q)])))]
    :tags ["GF"]}
   {:title "Conde 2"
    :description "As always, unbound variables in each conde clause are
                 'fresh'. Each clause starts the 'fresh count' anew for
                 that conde branch."
    :tests '[(= __ (run* [a b c]
                     (conde
                      [(== a :foo)
                       (== b :bar)]
                      [(== :tambourine b)
                       (== c "stingray")])))]}
   {:title "Conde 3"
    :description "What happens if one of the clauses contains a
                  contradiction?"
    :tests '[(= __ (run* [q]
                     (conde
                      [(== q :foo)]
                      [(== :bar q) (== q :baz)])))]
    :tags ["GF"]}
   {:title "Conde 4"
    :description "A unification failure in one part of a conde clause
                  causes the whole clause to fail"
    :tests '[(= __ (run* [q]
                     (conde
                      [(== q :foo) (== :baz :blizzles)]
                      [(== :tim :tim) (== :bar q)])))]
    :tags ["GF"]}
   {:title "Conde 5"
    :description "What's going on here? How many clauses is that? What
                   the I don't even...."
    :tests '[(= __ (run* [q]
                     (conde [fail succeed])))]
    :tags ["TRS"]}
   {:title "Conde 6"
    :description "Write a conde clause such that the program
                   evalues to [:tim :buck :two]. You should
                   replace '__' with '(conde ....)'"
    :tests '[(= [:tim :buck :two] (run* [q] __))]
    :tags ["GF"]}
   {:title "Run 1"
    :description "When you use run instead of run* you can limit the
                  results to a particular number."
    :tests '[(= __ (run 1 [q]
                     (conde
                      [(== q :foo)]
                      [(== :bar q)])))]
    :tags ["TRS"]}
   {:title "Conde 7"
    :description "For advanced beginners only."
    :tests '[(= __ (run* [q]
                     (conde
                      [(== :virgin q) fail]
                      [(== :olive q) succeed]
                      [succeed succeed]
                      [(== :oil q) succeed]
                      [succeed fail])))]
    :tags ["TRS"]}
   {:title "Conde 8"
    :description "Who's hungry for some soup?"
    :tests '[(= __ (run* [r]
                     (fresh [x y]
                       (conde
                        [(== :split x) (== :pea y)]
                        [(== :navy x) (== :bean y)])
                       (== [x y] r))))]
    :tags ["TRS"]}
   {:title "Oh man that's trippy."
    :description "Wait we're giving local names to several different
                  goals? Why would we even do that? Is this supposed
                  to illustrate something about how the code is
                  evaluated? IS THIS ALL JUST FOR LEARNINGS??"
    :tests '[(= __ (run* [q]
                     (let [a (== true q),
                           b (fresh [x]
                               (== x q)
                               (== false x))
                           c (conde
                              [(== true q) succeed]
                              [(== false q)])]
                       b)))]
    :tags ["TRS"]}
   {:title "Sequences 1"
    :description "Firsto is a relation on sequences where the second
                  argument is the first element of the first
                  argument. It is the relational version of
                  clojure.core/first."
    :tests '[(= __ (run* [r]
                     (firsto '(a c o r n) r)))]
    :tags ["TRS"]}
   {:title "Sequences 2"
    :description "Oh yeah, well why don't YOU try writing the
                  descriptions for all of these problems?"
    :tests '[(= __ (run* [q]
                     (firsto '(a c o r n) 'a)
                     (== true q)))]
    :tags ["TRS"]}
   {:title "Sequences 3"
    :description "What if the list has logic variables in it?
                  CAN ALL THIS REALLY WORK??"
    :tests '[(= __ (run* [r]
                     (fresh [x y]
                       (firsto [r y] x)
                       (== :pear x))))]
    :tags ["TRS"]}
   {:title "Sequences 4"
    :description "Conso is the relational version of cons.  (conso a b
                  c) declares that c is a sequence whose first element
                  is a and whose rest is b."
    :tests '[(= __ (run* [r]
                     (fresh [x y]
                       (firsto '(grape raisin pear) x)
                       (firsto '((a) (b) (c)) y)
                       (conso x y r))))]
    :tags ["TRS"]}
   {:title "Sequences 5"
    :description "Resto is the relational version of rest."
    :tests '[(= __ (run* [r]
                     (fresh [v]
                       (resto '(a c o r n) v)
                       (firsto v r))))]
    :tags ["TRS"]}
   {:title "Sequences 6"
    :description "Try to write a relation that succeeds when its input
                  is a list. Note that strings are not considered
                  sequences by the core.logic unifier."
    :tests '[(= '(_0) (run 1 [q] (__ [7 8 9])))
             (= () (run 1 [q] (__ 42)))
             (= '([[:foo]]) (run 1 [q] (__ q) (== q [[:foo]])))
             (= '() (run 1 [q] (__ q) (== q "seventeen")))]
    :tags ["GF"]}
   {:title "Matche 1"
    :description "Matche is a pattern-matching goal that can largely
                  replace tedious uses of fresh, firsto, resto,
                  etc. See also defne.  The clauses are tested
                  independently, like conde."
    :tests '[(= __ (run* [q]
                     (matche [:foo]
                             ([:foo] (== q 15))
                             ([:bar] (== q 42)))))]
    :tags ["GF"]}
   {:title "Matche 2"
    :description "This is another example with matche."
    :tests '[(= __ (run* [q]
                     (matche [q]
                             ([:achey])
                             ([:breaky] (== q :tamborine))
                             ([:heart] (== :heart q)))))]
    :tags ["GF"]}
   {:title "Matche 3"
    :description "Moar matche!"
    :tests '[(= __ (run* [q]
                     (matche [q]
                             ([[1 2 3 . more]] (== more [7 8 9]))
                             ([[a b c]] (== b "bee") (== c b))
                             ([[[]]] (== q 42)))))]
    :tags ["GF"]}
   {:title "Matche 4"
    :description "Pattern matching lets us implicitely unify two things
                  by giving them the same name."
    :tests '[(= __ (run* [a b c]
                     (matche [a b]
                             ([17 [x]] (== x c))
                             ([x x]))))]
    :tags ["GF"]}
   {:title "Branching"
    :description "So many possibilities! (Note the return value is a set, so you
                  don't have to worry about ordering)"
    :tests '[(= __ (set
                    (let [good-number (fn [x]
                                        (conde [(== 42 x)] [(== x 1024)]))
                          somewhat-nested (fn [x]
                                            (matche [x] ([[y]]) ([[[y]]])))]
                      (run* [a b]
                        (good-number a)
                        (somewhat-nested b)))))]}
   ;; {:title "Writing relations 1"
   ;;  :description "Write a relation that succeeds when its input
   ;;                is a list of doubly-repeated elements."
   ;;  :tests '[(= ['_0] (letfn [(twinsies [x] __)]
   ;;                      (run* [q]
   ;;                        (twinsies [:a :a :b :b :c :c])
   ;;                        (twinsies []))))
   ;;           (= [] (letfn [(twinsies [x] __)]
   ;;                   (run* [q]
   ;;                     (twinsies [:a :a :b :b :c :c 42]))))
   ;;           (= [[:a :b]] (letfn [(twinsies [x] __)]
   ;;                          (run* [x y] (twinsies [7 7 :a x y :b]))))]
   ;;  :tags ["GF"]}
   ;; {:title "Writing relations 2"
   ;;  :description "Write a relation that succeeds when its input
   ;;                is a list of pairs of elements."
   ;;  :tests '[(= ['_0]
   ;;              (letfn [(twinsies [x] __)]
   ;;                (run* [q]
   ;;                  (twinsies [[:a :a] [:b :b] [:c :c]]) (twinsies []))))
   ;;           (= [] (letfn [(twinsies [x] __)]
   ;;                   (run* [q] (twinsies [[:a :a] [:b :b] [:c :c] 42]))))
   ;;           (= [[:a :b]] (letfn [(twinsies [x] __)]
   ;;                          (run* [x y] (twinsies [[7 7] [:a x] [y :b]]))))]
   ;;  :tags ["GF"]}

   ;; TODO: maps and partial-map?
   ;; TODO: defrel & facts?
   ;; TODO: finite domains?
   #_{:title ""
      :description ""
      :tests '[]
      :tags ["TRS"]}])
