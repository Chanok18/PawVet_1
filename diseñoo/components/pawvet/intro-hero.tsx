import { Calendar, ChevronRight, MapPin } from "lucide-react"

export function IntroHero() {
  return (
    <section className="space-y-5">
      <h1 className="anim-hero font-heading text-[clamp(2.4rem,8vw,3.8rem)] leading-[0.96] font-semibold text-foreground text-balance">
        El cuidado que tu mascota{" "}
        <span className="text-primary">merece</span>, en un solo lugar.
      </h1>

      {/* Next appointment — glass card */}
      <article className="anim-hero relative overflow-hidden rounded-3xl bg-primary p-5 text-primary-foreground shadow-float">
        <div
          aria-hidden
          className="pointer-events-none absolute -right-10 -top-12 size-44 rounded-full bg-[radial-gradient(circle_at_center,oklch(0.7_0.12_165/0.5),transparent_70%)]"
        />
        <div className="relative flex items-center justify-between">
          <span className="inline-flex items-center gap-1.5 rounded-full bg-primary-foreground/15 px-3 py-1 text-xs font-medium backdrop-blur">
            <span className="size-1.5 rounded-full bg-primary-foreground" />
            Próxima cita
          </span>
          <span className="font-mono text-xs text-primary-foreground/70">
            En 2 días
          </span>
        </div>

        <p className="relative mt-4 font-heading text-[clamp(1.35rem,4vw,1.8rem)] font-semibold">
          Chequeo general · Milo
        </p>

        <div className="relative mt-3 flex flex-wrap items-center gap-x-4 gap-y-1.5 text-sm text-primary-foreground/85">
          <span className="inline-flex items-center gap-1.5">
            <Calendar className="size-4" strokeWidth={1.75} />
            Vie 27 Jun · 10:30
          </span>
          <span className="inline-flex items-center gap-1.5">
            <MapPin className="size-4" strokeWidth={1.75} />
            Sede Centro
          </span>
        </div>

        <button
          type="button"
          className="relative mt-4 inline-flex w-full items-center justify-between rounded-2xl bg-primary-foreground/12 px-4 py-3 text-sm font-medium backdrop-blur transition-colors hover:bg-primary-foreground/20"
        >
          Ver detalles de la cita
          <ChevronRight className="size-4" strokeWidth={2} />
        </button>
      </article>
    </section>
  )
}
