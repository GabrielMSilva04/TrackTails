import { useParams } from 'react-router-dom';
import Map from '../components/Map';

export default function MapDetails() {
    const { animalName } = useParams();

    return (
        <div className="h-full">
            <Map animalName={animalName} />
        </div>
    );
}