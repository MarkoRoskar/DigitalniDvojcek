import useMap from "react";
import L from "leaflet";
import { useEffect } from "react";

function MapButton() {
    const map = useMap();

    useEffect(() => {
        if (!map) return;

        var button = L.control({
            position: "topright"
        });

        button.onAdd = function(map) {
            this._div = L.DomUtil.create("div", "myControl");
            const buttonElement = "<div class='btnWrapper'><button>MY BUTTON</button></div>";
            
            this._div.innerHTML = buttonElement;
            return this._div;
        };

        button.addTo(map);
        return () => map.remove(button);
    }, [map]);

    return null;
}

export default MapButton;