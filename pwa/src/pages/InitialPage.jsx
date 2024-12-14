import { NavLink } from "react-router-dom";

export default function InitialPage() {
    return (
        <>
            {/* Container principal */}
            <div className="relative w-full h-screen flex flex-col">
                {/* Seção de imagem */}
                <div
                    className="w-full h-3/4 bg-cover bg-center"
                    style={{
                        backgroundImage: "url('https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.federationcouncil.nsw.gov.au%2Ffiles%2Fassets%2Fpublic%2Fimage-library%2Fgeneral-pages%2Fanimals%2Fdog-puppy-on-garden.jpg%3Fw%3D1200&f=1&nofb=1&ipt=6bdcd051c84a81dd47377c5a404a3706beec5fa0ee18b6de581a566444791300&ipo=images')",
                    }}
                >
                    <div className="w-full h-full bg-black bg-opacity-50 flex justify-center items-center">
                    </div>
                </div>

                {/* Seção inferior com botão */}
                <div className="bg-primary w-full h-2/4 flex flex-col justify-center items-center">
                    <h1 className="text-white text-2xl mb-8 font-bold text-center">
                        Take care of your pet with us.
                    </h1>
                    <NavLink to="login" className="btn btn-secondary text-white rounded-lg text-lg font-semibold shadow-lg">
                        Get Started
                    </NavLink>
                </div>
            </div>
        </>
    );
}
