export default function Navbar() {
    return (
        <header className="h-14 bg-white border-b flex items-center justify-between px-6">

            <div className="font-semibold">
                Plataforma hábitos saludables
            </div>

            <div className="flex items-center gap-4">
                <span>Profesional</span>
                <button className="text-red-500">Salir</button>
            </div>

        </header>
    );
}