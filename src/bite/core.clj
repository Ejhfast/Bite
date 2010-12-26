(ns bite.core
  (:use ring.adapter.jetty
        ring.middleware.params))

(defn route [routes]
  (fn [req]
    ))
