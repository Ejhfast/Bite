(ns bite.core
  (:use ring.adapter.jetty
        ring.middleware.params))

;; helper routes

(defn bad-uri [req]
  "Default handler for unmatched requests"
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body "Bad Request"
   })

;; generic helper functions

(defn compose-list [funcs arg]
     (if (not (empty? funcs))
       (let [nxt-f (first funcs)
             rst (drop 1 funcs)
             next (apply nxt-f [arg])]
         (recur rst next))
       arg))

;; Main app-building functions

(defn route [routes]
  "Build a function which routes a request to the correct handler."
  "E.g. (def myapp (route
                     {\"/*\"       {:get home-fn
                                    :post process-fn}
                      \"/user/.*\" {:get user-fn}}))"
  (fn [req]
    (let [uri (:uri req)
          req-m (:request-method req)
          mddl (:middleware routes)
          r-keys (map re-pattern (keys routes))
          ;; match routes against the request uri
          matches (filter #(re-seq %1 uri) r-keys)
          ;; match against request-method
          m-funs-h (map #(get routes (str %1)) matches)
          m-funs (map #(get %1 req-m) m-funs-h)
          ;; get first matching route
          res-fun (first (concat m-funs [bad-uri]))]
      ;; apply middleware to route, if any exists
      (apply (compose-list mddl res-fun) [req]))))


;; Testing 

(defn idem-p [req] (merge req {:special "true"}))
(defn idem [req] req))

(def app (route
          {
           ;; A regex to match on uris
           "/user/.*"
           ;; Hash that maps to various handlers
           {:get idem-p
            :post idem}
           ;; Another regex, for second uri ... 
           "/index"
           {:get idem-p
            :post idem}
           ;; Apply various bits of middleware   
           :middleware
           [wrap-params] }))


      
