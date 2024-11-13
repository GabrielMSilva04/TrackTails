// src/components/TimeRangeSelector.jsx
import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCalendarAlt } from "@fortawesome/free-solid-svg-icons";

const TimeRangeButton = ({ label, isActive, onClick, icon }) => {
  return (
    <button
      onClick={onClick}
      className={`btn btn-xs btn-outline m-1 rounded-lg
        ${isActive ? "btn-primary text-white" : "btn-secondary text-gray-600"}
      `}
    >
      {icon ? <FontAwesomeIcon icon={icon} className="text-lg" /> : label}
    </button>
  );
};

const TimeRangeSelector = ({ onSelect }) => {
  const [activeRange, setActiveRange] = useState("24h");

  // List of available ranges
  const ranges = ["1h", "24h", "1w", "1M", "3M", "1Y", "MAX"];
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
      <TimeRangeButton
        icon={customButtonIcon}
        isActive={activeRange === "Custom"}
        onClick={() => handleSelect("Custom")}
      />
    </div>
  );
};

export default TimeRangeSelector;
