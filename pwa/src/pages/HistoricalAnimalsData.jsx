// src/pages/HistoricalAnimalsData.jsx
import React from "react";
import { useParams } from "react-router-dom";
import ChartComponent from "../components/Chart";
import TimeRangeSelector from "../components/TimeRangeSelector"

const HistoricalAnimalsData = () => {
  const { metric } = useParams();

  const selectHandler = (range) => {
    console.log(range);
  }

  /* Mock data */
  const data = {
    labels: ["Janeiro", "Fevereiro", "Março", "Abril", "Maio"],
    datasets: [
      {
        label: "Vendas 2024",
        data: [30, 50, 80, 45, 90],
        backgroundColor: [
          "rgba(255, 99, 132, 0.5)",
          "rgba(55, 55, 55, 0.5)",
          "rgba(255, 206, 86, 0.5)",
          "rgba(75, 192, 192, 0.5)",
          "rgba(153, 102, 255, 0.5)",
        ],
        borderColor: [
          "rgba(255, 99, 132, 1)",
          "rgba(54, 162, 235, 1)",
          "rgba(255, 206, 86, 1)",
          "rgba(75, 192, 192, 1)",
          "rgba(153, 102, 255, 1)",
        ],
        borderWidth: 1,
      },
    ],
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
  };

  return (
    <div className="flex flex-col items-center">
      <select className="select select-sm select-bordered w-full max-w-xs m-auto">
        <option value="1">Janeiro</option>
        <option value="2">Fevereiro</option>
        <option value="3">Março</option>
        <option value="4">Abril</option>
        <option value="5">Maio</option>
      </select>
      <div className="h-64">
        <ChartComponent type="bar" data={data} options={options} />
      </div>
      <TimeRangeSelector onSelect={selectHandler} />
    </div>
  );
};

export default HistoricalAnimalsData;
