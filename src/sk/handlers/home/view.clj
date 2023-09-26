(ns sk.handlers.home.view
  (:require [sk.models.util :refer [build-button build-field build-form]]
            [hiccup.core :refer [html]]
            [sk.handlers.home.model :refer [get-rows ventas-rows somos-rows]]))

(def rows
  [{:enlace "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.8IOxLmHP1R1ZjM-aSaV-uwHaHa%26pid%3DApi&f=1&ipt=b417a5d65553c9bee3332a90764a20f46f2d17feca99e66da9cdbfd9abc05b69&ipo=images" :first 1}
   {:enlace "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.txh5nfOS2GZN3-JvKDawbAHaHa%26pid%3DApi&f=1&ipt=0a423c2adbd960d312527b9042bce36252c97cc4d002f07c647ab61f96be46fb&ipo=images" :first 0}
   {:enlace "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.PMTqXZLAsw4PGn_bitOtIAHaHa%26pid%3DApi&f=1&ipt=2583ee31911cdc78b5a6c7fbbffdfe0500b70e3ff0bf170c5cb61658e5d08097&ipo=images" :first 0}
   {:enlace "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.explicit.bing.net%2Fth%3Fid%3DOIP.9IwmGq8xQazw6NckmLx9vgHaHa%26pid%3DApi&f=1&ipt=669fc75ca51c71749ec2fa156f0ab9d30385a5cf6eeced0bb234e58e4aa382f9&ipo=images" :first 0}
   {:enlace "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.OZbUBdFctsY3FVoDc118TgHaHa%26pid%3DApi&f=1&ipt=c93b2680b99d798bad1bd0844446f5846c1664f7e42cb9720fc17d8f5d7b8e12&ipo=images" :first 0}
   {:enlace "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.8taJGZE2dcdSu_b8FW-1zQHaHa%26pid%3DApi&f=1&ipt=44a1b6f2ad7cd579b964a4e4c83bbcb351d31bb0935363f48822f67d0d31d875&ipo=images" :first 0}])

(defn slideshow-body [row]
  (list
   (if (= (:first row) 1)
     [:div.carousel-item.active [:img.img-fluid {:src (:enlace row)
                                                 :alt "CM"}]]
     [:div.carousel-item [:img.img-fluid {:src (:enlace row)
                                          :alt "CM"}]])))

(defn build-slideshow []
  (list
   [:div.carousel.slide {:id "cm"
                         :data-ride "carousel"}
    [:div.carousel-inner
     (map slideshow-body rows)]]))

(defn main-view []
  (html
   [:div.container
    (build-slideshow)]))

(defn build-main-body [row]
  (let [header (:d1 row)
        detail (:d2 row)
        imagen (:imagen row)]
    (list
     [:div.col-sm
      [:div.card {:style "width: 18rem;"}
       [:div.card-body
        [:h5.card-header [:img {:src (str "/images/" imagen)
                                :style "margin-right:2px;"
                                :alt "."}] header]
        [:p.card-text detail]]]])))

(defn asistencia-view []
  (list
   [:div.container {:style "margin-top:30px;"}
    [:div.row
     (map build-main-body (get-rows))]]))

(defn ventas-view []
  (list
   [:div.container {:style "margin-top:30px;"}
    [:div.row
     (map build-main-body (ventas-rows))]]))

(defn somos-view []
  (list
   (map (fn [row]
          [:div.jumbotron.jumbotron-fluid
           [:div.container
            [:h1.display-4 (:empresa row)]
            [:p (:meta row)]]]) (somos-rows))))

(defn login-view [token]
  (build-form
   "Conectar"
   token
   (list
    (build-field
     {:id "username"
      :name "username"
      :class "easyui-textbox"
      :prompt "Email aqui..."
      :validType "email"
      :data-options "label:'Email:',labelPosition:'top',required:true,width:'100%'"})
    (build-field
     {:id "password"
      :name "password"
      :class "easyui-passwordbox"
      :prompt "Contraseña aqui..."
      :data-options "label:'Contraseña:',labelPosition:'top',required:true,width:'100%'"})
    (build-button
     {:href "javascript:void(0)"
      :id "submit"
      :text "Acceder al sitio"
      :class "easyui-linkbutton c6"
      :onClick "submitForm()"}))
   (list
    [:div {:style "margin-bottom:10px;"}
     [:a {:href "/register"} "Clic para registrarse"]]
    [:div {:style "margin-bottom:10px;"}
     [:a {:href "/rpaswd"} "Clic para resetear su contraseña"]])))

(defn login-script []
  [:script
   "
    function submitForm() {
        $('.fm').form('submit', {
            onSubmit:function() {
                if($(this).form('validate')) {
                  $('a#submit').linkbutton('disable');
                  $('a#submit').linkbutton({text: 'Processando!'});
                }
                return $(this).form('enableValidation').form('validate');
            },
            success: function(data) {
                try {
                    var dta = JSON.parse(data);
                    if(dta.hasOwnProperty('url')) {
                        window.location.href = dta.url;
                    } else if(dta.hasOwnProperty('error')) {
                        $.messager.show({
                            title: 'Error: ',
                            msg: dta.error
                        });
                        $('a#submit').linkbutton('enable');
                        $('a#submit').linkbutton({text: 'Acceder al sitio'});
                    }
                } catch(e) {
                    console.error('Invalid JSON');
                }
            }
        });
    }
   "])
