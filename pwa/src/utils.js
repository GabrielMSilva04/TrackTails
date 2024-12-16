export function checkToken() {
	const token = localStorage.getItem("authToken");
	if (!token || token === "undefined") {
		return false;
	}
	return true;
}

export function range2(start, end, step) {
	// if only one argument is provided, the function will use it as the end value
	if (end === undefined) {
		end = start;
		start = 0;
	}
	// if only two arguments are provided, the function will use 1 as the step value
	if (step === undefined) {
		step = 1;
	}
	// create an empty array to store the result
	const result = [];
	// iterate over the range from start to end
	for (let i = start; i < end; i += step) {
		// push the current value to the result array
		result.push(i);
	}
	// return the result array
	return result;
}


export function convertToInfluxDBFormat(inputDatetime) {
	const date = new Date(inputDatetime);
  
	const year = date.getUTCFullYear();
	const month = String(date.getUTCMonth() + 1).padStart(2, '0');
	const day = String(date.getUTCDate()).padStart(2, '0');
	const hours = String(date.getUTCHours()).padStart(2, '0');
	const minutes = String(date.getUTCMinutes()).padStart(2, '0');
	const seconds = String(date.getUTCSeconds()).padStart(2, '0');
  
	return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}Z`;
}
