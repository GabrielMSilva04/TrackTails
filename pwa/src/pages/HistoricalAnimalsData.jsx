// src/pages/HistoricalAnimalsData.jsx
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import ChartComponent from "../components/Chart";
import TimeRangeSelector from "../components/TimeRangeSelector"
import { range2 } from "../utils";
import axios from "axios";

const base_url = "http://localhost:8082/api/v1";

const HistoricalAnimalsData = () => {
  const [chartType, setChartType] = useState("Line");
	const [range, setRange] = useState("24H");
	const [data, setData] = useState({
    labels: [],
    datasets: [],
	});
	const [options, setOptions] = useState({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: false,
      },
    },
		scales: {
			x: {
				type: "time",
				time: {
					unit: "hour",
				},
			}
		}
	})

	const setLabels = (labels) => {
		setData((prevData) => ({ ...prevData, labels }));
	};

	const addDataset = (dataset) => {
		console.log("Current data", data);
		console.log("Adding dataset", dataset);
		setData((prevData) => ({
			...prevData,
			datasets: [...prevData.datasets, dataset],
		}));
		console.log("Updated data", data);
	};

	const clearDatasets = () => {
		setData((prevData) => ({
			...prevData,
			datasets: [],
		}));
	};

	const setChartTitle = (title) => {
		setOptions((prevOptions) => ({
			...prevOptions,
			plugins: {
				...prevOptions.plugins,
				title: {
					...prevOptions.plugins.title,
					text: title,
				},
			},
		}));
	};

	const hideScales = () => {
		setOptions((prevOptions) => ({
			...prevOptions,
			scales: {
				x: {
					type: "time",
					time: {
						unit: "hour",
					},
					display: true,
				},
			},
		}));
	};

	const updateScalesInterval = (range) => {
		const range_to_timestampe_map = {
			"1H": 60 * 60 * 1000,
			"24H": 24 * 60 * 60 * 1000,
			"1W": 7 * 24 * 60 * 60 * 1000,
			"1M": 30 * 24 * 60 * 60 * 1000,
			"3M": 90 * 24 * 60 * 60 * 1000,
			"1Y": 52 * 7 * 24 * 60 * 60 * 1000,
		};

		setOptions((prevOptions) => ({
			...prevOptions,
			scales: {
				x: {
					...prevOptions.scales.x,
					min: (Date.now() - range_to_timestampe_map[range]),
					max: Date.now(),
				},
			},
		}));
	}

	const showScales = () => {
		setOptions((prevOptions) => ({
			...prevOptions,
			scales: {
				x: {
					type: "time",
					time: {
						unit: "hour",
					},
					display: true,
				},
			},
		}));
		updateScalesInterval(range);
	}

	const setChartScaleType = (t) => {
		setOptions((prevOptions) => ({
			...prevOptions,
			scales: {
				...prevOptions.scales,
				x: {
					...prevOptions.scales.x,
					type: t,
				},
			},
		}));
	};

	const setChartScaleUnit = (unit) => {
		setOptions((prevOptions) => ({
			...prevOptions,
			scales: {
				...prevOptions.scales,
				x: {
					...prevOptions.scales.x,
					time: {
						...prevOptions.scales.x.time,
						unit,
					},
				},
			},
		}));
	};

	const fetchAnimalData = async (animal, metric) => {
		const range_to_interval_map = {
			"1H": "1m",
			"24H": "1h",
			"1W": "1d",
			"1M": "1d",
			"3M": "1d",
			"1Y": "1w",
		};

		// Fetch data from API
		const response = await axios.get(`${base_url}/animaldata/historic/${animal}/${metric}?start=-${range.toLowerCase()}&interval=${range_to_interval_map[range]}`);
		if (response.status !== 200) {
			console.error("Failed to fetch data from API");
			return;
		}
		console.log(response.data);
		setLabels(response.data.map((d) => d.timestamp));
		clearDatasets();
		console.log(data);
		addDataset({
			label: metric,
			data: response.data.map((d) => d[metric]),
			backgroundColor: "#4d7c0f",
			borderColor: "rgba(54, 83, 20, 1)",
			borderWidth: 2,
		});
		console.log(data);
	}

	useEffect(() => {
		showScales();
	}, []);

	useEffect(() => {
		fetchAnimalData("12345", "weight");
	}, [range]);

  const timeRangeSelectHandler = (range) => {
		const range_to_unit_map = {
			"1H": "minute",
			"24H": "hour",
			"1W": "hour",
			"1M": "day",
			"3M": "day",
			"1Y": "week",
		};
	  	
		setRange(range);
		setChartScaleUnit(range_to_unit_map[range]);
		updateScalesInterval(range);
  }

  const chartTypeSelectorHandler = (event) => {
		switch (event.target.value) {
			case "1":
				setChartType("line");
				showScales();
				break;
			case "2":
				setChartType("line");
				showScales();
				break;
			case "3":
				setChartType("pie");
				hideScales();
				break;
			default:
				setChartType("line");
  	}
	};
	


  return (
    <div className="flex flex-col items-center mt-2">
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
