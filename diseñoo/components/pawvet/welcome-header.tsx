import Image from "next/image"
import { Bell } from "lucide-react"

export function WelcomeHeader() {
  return (
    <header className="anim-header flex items-center justify-between gap-4 pt-2">
      <div className="flex items-center gap-3">
        <div className="relative">
          <div className="size-12 overflow-hidden rounded-2xl ring-2 ring-card shadow-soft">
            <Image
              src="/images/pet-avatar.png"
              alt="Tu mascota, Milo"
              width={96}
              height={96}
              className="size-full object-cover"
            />
          </div>
          <span className="absolute -bottom-0.5 -right-0.5 size-3.5 rounded-full border-2 border-background bg-emerald" />
        </div>
        <div className="leading-tight">
          <p className="text-xs font-medium tracking-wide text-muted-foreground">
            Buenos días, Lucía
          </p>
          <p className="font-heading text-[1.35rem] font-semibold leading-none text-foreground">
            PawVet<span className="text-primary">.</span>
          </p>
        </div>
      </div>

      <button
        type="button"
        aria-label="Notificaciones"
        className="relative grid size-11 place-items-center rounded-2xl border border-border/70 bg-card shadow-soft transition-transform active:scale-95"
      >
        <Bell className="size-5 text-foreground" strokeWidth={1.75} />
        <span className="absolute right-2.5 top-2.5 size-2 rounded-full bg-coral ring-2 ring-card" />
      </button>
    </header>
  )
}
