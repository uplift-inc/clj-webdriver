;; Browser window management support
(ns clj-webdriver.window
	(:import [org.openqa.selenium Dimension Point]
		     [clj_webdriver.driver Driver]))

(defprotocol IWindow
	(position [driver] "Returns map of X Y coordinates ex. {:x 1 :y 3} relative to the upper left corner of screen.")
	(reposition! [driver point-map] "Excepts map of X Y coordinates ex. {:x 1 :y 3} repositioning current window relative to screen.")
	(size [driver] "Get size of current window; returns a map of width and height ex. {:width 480 :height 800}")
	(resize! [driver dim-map] "Resize the driver window with a map of width and height ex. {:width 480 :height 800}.")
	(maximize! [driver] "Maximizes the current window to fit screen if it is not already maximized"))

(defn- get-window
	[driver]
	(-> (:webdriver driver)
		(.manage)
		(.window)))

(extend-type Driver
	IWindow
	(position [driver]
		(let [wnd (get-window driver)
			  pnt (.getPosition wnd)]
			{:x (.getX pnt) :y (.getY pnt)}))

	(reposition! [driver point-map]
		(let [{:keys [x y]} point-map
			  wnd (get-window driver)]
			(.setPosition wnd (Point. x y))))

	(size [driver]
		(let [wnd (get-window driver)
			  dim (.getSize wnd)]
			{:width (.getWidth dim) :height (.getHeight dim)}))

	(resize! [driver dim-map]
		(let [{:keys [width height]} dim-map
			  wnd (get-window driver)]
			(.setSize wnd (Dimension. width height))))

	(maximize! [driver]
		(let [wnd (get-window driver)]
			(.maximize wnd))))
