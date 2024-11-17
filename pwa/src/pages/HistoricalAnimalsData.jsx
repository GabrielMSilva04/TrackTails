// src/pages/HistoricalAnimalsData.jsx
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import ChartComponent from "../components/Chart";
import TimeRangeSelector from "../components/TimeRangeSelector"
import { range2 } from "../utils";

const HistoricalAnimalsData = () => {
  const [chartType, setChartType] = useState("Line");
	const [data, setData] = useState({
    labels: ['2024-01-01', '2024-01-02', '2024-01-03', '2024-01-04', '2024-01-05'],
    datasets: [
      {
        label: "Vendas 2024",
        data: [30, 50, 80, 45, 90],
        backgroundColor: "#4d7c0f",
		borderColor: "rgba(54, 83, 20, 1)",
        borderWidth: 2,
      },
    ],
	});

	const setLabels = (labels) => {
		setData((prevData) => ({ ...prevData, labels }));
	};

	const setDatasetProp = (prop, value) => {
		setData((prevData) => ({
			...prevData,
			datasets: prevData.datasets.map((dataset) => ({
				...dataset,
				[prop]: value,
			})),
		}));
	};

  const timeRangeSelectHandler = (range) => {
		const range_to_labels_map = {
			"1H": range2(60),
			"24H": range2(24),
			"1W": range2(7),
			"1M": range2(30),
			"3M": range2(90),
			"1Y": range2(52),
		};
		console.log(range);

		const labels = range_to_labels_map[range];
		console.log(labels);
		setLabels(labels);
		setDatasetProp("data", labels.map(() => Math.floor(Math.random() * 100)));
  }

  const chartTypeSelectorHandler = (event) => {
		switch (event.target.value) {
			case "1":
				setChartType("line");
				break;
			case "2":
				setChartType("line");
				break;
			case "3":
				setChartType("pie");
				break;
			default:
				setChartType("line");
  	}
	};
	


  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: "Gráfico Genérico",
      },
    },
		scales: {
			x: {
				type: "time",
				time: {
					unit: "second",
				},
			}
		}
  };

  return (
    <div className="flex flex-col items-center">
      <select className="select select-sm select-bordered w-full max-w-xs m-auto" onChange={chartTypeSelectorHandler}>
        <option value="1">Instant - Line Chart</option>
        <option value="2">Average - Line Chart</option>
        <option value="3">Distribution - Pie chart</option>
      </select>
      <div className="h-64">
        <ChartComponent type={chartType} data={data} options={options} />
      </div>
      <TimeRangeSelector onSelect={timeRangeSelectHandler} />
    </div>
  );
};

export default HistoricalAnimalsData;
