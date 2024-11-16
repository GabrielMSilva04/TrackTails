// src/components/TimeRangeSelector.jsx
import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCalendarAlt } from "@fortawesome/free-solid-svg-icons";

const TimeRangeButton = ({ label, isActive, onClick, icon }) => {
  return (
    <button
      onClick={onClick}
      className={`btn btn-xs m-1 rounded-lg
        ${isActive ? "btn-primary text-white" : "btn-secondary btn-outline text-gray-600"}
      `}
    >
      {icon ? <FontAwesomeIcon icon={icon} className="text-lg" /> : label}
    </button>
  );
};

const CustomTimeRangeDropdown = ({ onSelect, isActive }) => {
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  const handleApply = () => {
    if (startDate && endDate) {
      // Verify if startDate is before endDate
      if (new Date(startDate) > new Date(endDate)) {
        return;
      }
      onSelect(`${startDate}-${endDate}`);
    }
  };

  return (
    <div className="dropdown dropdown-end">
      <label tabIndex={0} className={`btn btn-xs m-1 rounded-lg
        ${isActive ? "btn-primary text-white" : "btn-secondary btn-outline text-gray-600"}
      `}>
        <FontAwesomeIcon icon={faCalendarAlt} className="text-lg" />
      </label>
      <div tabIndex={0} className="dropdown-content p-4 shadow bg-base-200 rounded-lg w-64">
        <h3 className="text-sm font-semibold mb-2">Select a custom range</h3>
        <input
          type="datetime-local"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
          className="input input-bordered input-sm w-full mb-2"
        />
        <input
          type="datetime-local"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
          className="input input-bordered input-sm w-full mb-4"
        />
        <button
          onClick={handleApply}
          className="btn btn-sm btn-primary w-full"
          disabled={!startDate || !endDate}
        >
          Apply
        </button>
      </div>
    </div>
  );
};

const TimeRangeSelector = ({ onSelect }) => {
  const [activeRange, setActiveRange] = useState("24H");

  // List of available ranges
  const ranges = ["1H", "24H", "1W", "1M", "3M", "1Y", "MAX"];
  const customButtonIcon = faCalendarAlt;

  const handleSelect = (range) => {
    setActiveRange(range);
    if (onSelect) {
      onSelect(range);
    }
  };

  return (
    <div className="flex items-center justify-center py-4">
      {/* horizontal overflow */}
      <div className="flex overflow-x-auto space-x-2">
        <div className="flex flex-nowrap">
          {ranges.map((range) => (
            <TimeRangeButton
              key={range}
              label={range}
              isActive={activeRange === range}
              onClick={() => handleSelect(range)}
            />
          ))}
        </div>
      </div>
      {/* Fixed button */}
      <CustomTimeRangeDropdown
        isActive={!ranges.includes(activeRange)}
        onSelect={handleSelect} />
    </div>
  );
};

export default TimeRangeSelector;
