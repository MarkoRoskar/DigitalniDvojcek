import { useEffect, useRef, useState } from 'react';
import * as D3 from 'd3';
import '../PieChart.css';
import { pie } from 'd3';

function PieChart() {

    // pie chart hook
    const pieChart = useRef();

    useEffect(() => {
        fetch("https://api.jcdecaux.com/vls/v3/stations?apiKey=frifk0jbxfefqqniqez09tw4jvk37wyf823b5j1i&contract=maribor")
        .then(res => res.json())
        .then(data => {
            console.log(data);

            // getting all location numbers with their corresponding names and addresses
            let locationsByStreet = [];
            data.map(location => {
                const obj = { number: location.number, name: location.name, address: location.address, bikes: location.totalStands.availabilities.bikes, stands: location.totalStands.availabilities.stands };
                locationsByStreet.push(obj);
            })
            /*locationsByStreet.sort((a, b) => a.number - b.number);*/
            console.log(locationsByStreet);

            // getting only unique streets
            const streets = [...new Set(locationsByStreet.map(each => each.name.split(" ", 1).toString()))]
            console.log(streets);

            let CountsByStreet = [];

            streets.map(strt => {
                let street = strt;
                let count = 0;
                let bikeCount = 0;
                let standCount = 0;

                locationsByStreet.map(location => {
                    if (location.name.split(" ", 1) == street) {
                        count++;
                        bikeCount += location.bikes;
                        standCount += location.stands;
                    }
                });
                CountsByStreet.push({street: street, count: count, bikes: bikeCount, stands: standCount});
            });
            console.log(CountsByStreet);


            const pieData = D3.pie().value(d => d.count)(CountsByStreet);

            const arc = D3.arc().innerRadius(0).outerRadius(300);
            // defining pie colors
            const colors = D3.scaleOrdinal(["yellow", "blue", "green", "red", "orange", "cyan", "grey", "#15be4c", "#8c52cd", "#0f1833", "#85f2ad", "#f2ad85", "#0361d0", "#e5dcd6", "#cc3584", "#721220", "#660066"]);

            // defining size and position of svg
            const svg = D3.select(pieChart.current)
                .attr("width", 600)
                .attr("height", 600)
                .append("g")
                    .attr("transform", "translate(300, 300)")


            const toolDiv = D3.select("#d3-pie-chart")
                                .append("div")
                                .style("visibility", "hidden")
                                .style("position", "absolute")
                                .style("font-size", "25px")
                                .style("background", "#2458E7")
                                .style("color", "white")

            // drawing the pie chart
            svg.append("g")
                .selectAll("path")
                .data(pieData)
                .join("path")
                    .attr("d", arc)
                    .attr("fill", (d,i) => colors(i))
                    .attr("stroke", "white")
                    .on("mouseover", (e,d,i) => {
                        toolDiv.style("visibility", "visible")
                                .text(`${d.data.street}
                                    postajališča: ${d.data.count}
                                    kolesa: ${d.data.bikes}
                                    parkirna mesta: ${d.data.stands}`)

                    })
                    .on("mousemove", (e,d) => {
                        toolDiv.style("top", (e.pageY-50) + "px")
                               .style("left", (e.pageX-50) + "px")
                    })
                    .on("mouseout", () => {
                        toolDiv.style("visibility", "hidden")
                    })

        })
    }, [])

    return (
        <div>
            <h1 id="title">ŠTEVILO MBAJK POSTAJALIŠČ PO POSAMEZNIH ULICAH</h1>
            <div id="d3-pie-chart">
                <svg ref={pieChart}></svg>
            </div>
        </div>
    );
}

export default PieChart;