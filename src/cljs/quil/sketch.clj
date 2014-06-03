(ns quil.sketch)

(defmacro with-sketch [applet & body]
  `(binding [quil.sketch/*surface* ~applet]
     ~@body))


(def ^{:private true} 
	supported-features
	#{:no-start})


(defmacro defsketch
  [app-name & options]
  (let [opts (apply hash-map options)
        features (let [user-features (set (:features opts))]
                   (reduce #(assoc %1 %2 (contains? user-features %2)) {}
                           supported-features))]
    `(do
       (defn ^:export ~app-name []
         (quil.sketch/make-processing ~@(apply concat (seq (merge (dissoc opts :features) features)))))
       
       ~(when (not (:no-start features))
          `(quil.sketch/add-sketch-to-init-list ~app-name)))))
