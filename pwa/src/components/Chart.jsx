// src/components/ChartComponent.jsx
import React from "react";
import { Bar, Line, Pie, Doughnut, Radar, Scatter, Bubble } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
	TimeScale,
  BarElement,
  LineElement,
  PointElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
  RadialLinearScale,
  Filler,
} from "chart.js";
import 'chartjs-adapter-dayjs-4/dist/chartjs-adapter-dayjs-4.esm';

ChartJS.register(
  CategoryScale,
  LinearScale,
	TimeScale,
  BarElement,
  LineElement,
  PointElement,
  ArcElement,
  RadialLinearScale,
  Title,
  Tooltip,
  Legend,
  Filler
);

const chartTypes = {
  bar: Bar,
  line: Line,
  pie: Pie,
  doughnut: Doughnut,
  radar: Radar,
  scatter: Scatter,
  bubble: Bubble,
};

const ChartComponent = ({ type, data, options }) => {
  const Chart = chartTypes[type] || Line; // By default, render a Line chart

  return <Chart data={data} options={options} />;
};

export default ChartComponent;
