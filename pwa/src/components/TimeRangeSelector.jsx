// src/components/TimeRangeSelector.jsx
import React, { useState, useEffect, useRef } from "react";
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
  const [isOpen, setIsOpen] = useState(false);
  const [isFlipped, setIsFlipped] = useState(false); // Estado para controlar a direção do dropdown
  const dropdownRef = useRef(null);
  const buttonRef = useRef(null);

  const handleApply = () => {
    if (startDate && endDate) {
      // Verify if startDate is before endDate
      if (new Date(startDate) > new Date(endDate)) {
        return;
      }
      onSelect(`${startDate}-${endDate}`);
    }
  };

  useEffect(() => {
    if (isOpen && buttonRef.current && dropdownRef.current) {
      const buttonRect = buttonRef.current.getBoundingClientRect();
      const dropdownHeight = dropdownRef.current.offsetHeight;
      const spaceBelow = window.innerHeight - buttonRect.bottom;

      // Verifica se há espaço suficiente abaixo do botão
      setIsFlipped(spaceBelow < dropdownHeight);
    }
  }, [isOpen]);

  return (
    <div className="dropdown dropdown-end">
      <label
        tabIndex={0}
        ref={buttonRef}
        onClick={() => setIsOpen(!isOpen)}
        className={`btn btn-xs m-1 rounded-lg ${isActive ? "btn-primary text-white" : "btn-secondary btn-outline text-gray-600"}`}
      >
        <FontAwesomeIcon icon={faCalendarAlt} className="text-lg" />
      </label>

      {/* Ajuste de posição baseado no estado `isFlipped` */}
      <div
        ref={dropdownRef}
        className={`dropdown-content p-4 shadow bg-base-200 rounded-lg w-64 ${isOpen ? "" : "hidden"}`}
        style={{
          position: "absolute",
          top: isFlipped ? "auto" : "100%", // Se não houver espaço abaixo, mostrar acima
          bottom: isFlipped ? "100%" : "auto",
        }}
      >
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
