import { Phone, ShieldPlus } from "lucide-react"

export function EmergencySection() {
  return (
    <section className="anim-section relative overflow-hidden rounded-3xl border border-coral/30 bg-card p-5 shadow-float">
      {/* Soft warm glow anchored to the icon, not a filler blob */}
      <div
        aria-hidden
        className="pointer-events-none absolute -left-8 -top-10 size-40 rounded-full bg-[radial-gradient(circle_at_center,oklch(0.72_0.14_38/0.22),transparent_70%)]"
      />

      <div className="relative flex items-start gap-4">
        <div className="relative grid size-12 shrink-0 place-items-center rounded-2xl bg-coral text-coral-foreground shadow-soft">
          <ShieldPlus className="size-6" strokeWidth={1.9} />
          <span className="absolute inset-0 -z-10 animate-ping rounded-2xl bg-coral/40" />
        </div>

        <div className="min-w-0">
          <div className="flex items-center gap-2">
            <h2 className="font-heading text-[1.35rem] font-semibold text-foreground">
              Emergencias 24h
            </h2>
            <span className="inline-flex items-center gap-1 rounded-full bg-emerald/12 px-2 py-0.5 text-[0.7rem] font-medium text-emerald">
              <span className="size-1.5 rounded-full bg-emerald" />
              En línea
            </span>
          </div>
          <p className="mt-1 text-sm leading-relaxed text-muted-foreground">
            Nuestro equipo veterinario está disponible a toda hora. Si es urgente,
            te conectamos de inmediato.
          </p>
        </div>
      </div>

      <div className="relative mt-4 flex gap-3">
        <button
          type="button"
          className="inline-flex flex-1 items-center justify-center gap-2 rounded-2xl bg-coral px-4 py-3.5 text-sm font-semibold text-coral-foreground shadow-soft transition-transform active:scale-95"
        >
          <Phone className="size-4" strokeWidth={2.25} />
          Llamar ahora
        </button>
        <button
          type="button"
          className="rounded-2xl border border-border bg-background px-4 py-3.5 text-sm font-semibold text-foreground transition-colors hover:bg-secondary"
        >
          Ubicar sede
        </button>
      </div>
    </section>
  )
}
