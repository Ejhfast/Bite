(ns bite.core
  (:use ring.adapter.jetty
        ring.middleware.params))

(defn bad-uri [req]
  "Default handler for unmatched requests"
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body "Bad Request"
   })

(defn route [routes]
  "Build a function which routes a request to the correct handler."
  (fn [req]
    (let [uri (:uri req)
          r-keys (map re-pattern (keys routes))
          matches (filter #(re-seq %1 uri) r-keys)
          m-funs (map #(get routes (str %1)) matches)]
      (apply (first (concat m-funs [bad-uri])) [req]))))


;; Testing 

(defn idem-p [req] (merge req {:special "true"}))
(defn idem [req] req))

(def app (route
          {"/user/.*" idem-p
           "/index" idem }))


      
