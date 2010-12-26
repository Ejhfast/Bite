(ns bite.core
  (:use ring.adapter.jetty
        ring.middleware.params))

(defn route [routes]
  (fn [req]
    (let [uri (:uri req)
          r-keys (map re-pattern (keys routes))
          debug (println r-keys)
          matches (filter #(re-seq %1 uri) r-keys)
          debug (println matches)
          m-funs (map #(get routes (str %1)) matches)
          debug (println m-funs)]
      (if m-funs (apply (first m-funs) [req]) "404: No Matching Response"))))


(defn idem [req] (merge req {:special "true"}))

(def app (route
          {"/home" idem
           "/index" idem }
          ))


      
