// src/pages/HistoricalAnimalsData.jsx
import { useState, useEffect } from "react";
import ChartComponent from "../components/Chart";
import TimeRangeSelector from "../components/TimeRangeSelector"
import axios from "axios";
import { useAnimalContext } from "../contexts/AnimalContext";

const base_url = "http://localhost/api/v1";

const HistoricalAnimalsData = ({ metric }) => {
  const [chartType, setChartType] = useState("Line");
  const [range, setRange] = useState("24H");
  const [dataNotFound, setDataNotFound] = useState(false);
  const [aggregateMode, setAggregateMode] = useState(false);
  const { selectedAnimal } = useAnimalContext();
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
          max: null,
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
      "MAX": "1mo"
    };
    const range_api_map = {
      "1H": "1h",
      "24H": "1d",
      "1W": "1w",
      "1M": "1mo",
      "3M": "3mo",
      "1Y": "1y",
    };

    const colors = [["#4d7c0f", "rgba(54, 83, 20, 1)"], ["#f6ad55", "#f6ad55"], ["#80C4E9", "#80C4E9"], ["#f3722c", "#f3722c"], ["#f94144", "#f94144"]];
    let color_index = 0;

    let endpoints = [`${base_url}/animaldata/historic/${animal}/${metric}?start=-${range_api_map[range]}&interval=${range_to_interval_map[range]}`];
    if (range === "MAX") {
      endpoints[0] = `${base_url}/animaldata/historic/${animal}/${metric}?&interval=${range_to_interval_map[range]}`;
    }

    const aggregations = ["mean", "min", "max"];
    let aggregation_index = 0;
    if (aggregateMode) {
      // replicate 3 times
      endpoints = endpoints.concat(endpoints).concat(endpoints);
      endpoints = endpoints.map((endpoint, index) => {
        return `${endpoint}&aggregate=${aggregations[index]}`;
      });
      color_index = 1;
    }
    clearDatasets();

    // Fetch data from API
    for (let endpoint of endpoints) {
      await axios.get(endpoint, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("authToken")}`,
          },
        })
        .then((response) => {
          setDataNotFound(false);
          console.log(response.data);
          setLabels(response.data.map((d) => d.timestamp));
          console.log(data);
          addDataset({
            label: aggregateMode ? `${metric} - ${aggregations[aggregation_index++]}` : metric,
            data: response.data.map((d) => d[metric]),
            backgroundColor: colors[color_index][0],
            borderColor: colors[color_index][1],
            borderWidth: 2,
          });
          color_index++;
          console.log(data);
        })
        .catch((error) => {
          console.error("Failed to fetch data from API");
          if (error.response) {
            console.error("Error response", error.response);
          }
          setDataNotFound(true);
          return;
        });
    }
  }


  useEffect(() => {
    fetchAnimalData(selectedAnimal.id, metric);
  }, [range, aggregateMode]);

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
        setAggregateMode(false);
        setChartType("line");
        showScales();
        break;
      case "2":
        setAggregateMode(true);
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
    <div className="flex flex-col items-center">
      <select className="select select-sm select-bordered w-full max-w-xs m-auto" onChange={chartTypeSelectorHandler}>
        <option value="1">Instant - Line Chart</option>
        <option value="2">Average - Line Chart</option>
      </select>
      <div className="flex flex-col h-64">
        {dataNotFound ? (
          <div className="my-auto">
            <p>
              Data not found
            </p>
            <p>
              Please check the animal tracker device
            </p>
          </div>
        ) : (
          <ChartComponent type={chartType} data={data} options={options} />
        )}
      </div>
      <TimeRangeSelector onSelect={timeRangeSelectHandler} />
    </div>
  );
};

export default HistoricalAnimalsData;
