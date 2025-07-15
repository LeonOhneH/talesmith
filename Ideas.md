# Ideas

## Environmental Hazards

**Overview:** Certain rooms carry passive effects—poison mists, healing springs, crumbling floors—that trigger each turn.

**Player Impact:** Movement decisions matter; players may detour to exploit buffs or avoid attrition.

**Implementation Notes:**

- Create an enum HazardType { POISON, HEAL, FALL }.

- Extend Room with nullable hazard and hazardIntensity.

- In Game.nextTurn(), apply room hazard to all Creatures present.

- Builder hook:
```
new RoomBuilder()
  .withName("Toxic Grotto")
  .withHazard(HazardType.POISON, 2)
  .build
```

## Stance System

**Overview:** Player toggles between “Assault” (higher damage, lower defense) and “Guard” (vice‑versa) stances via a free action.

**Player Impact:** Adds tactical depth—anticipate enemy attack patterns and swap stance accordingly.

**Implementation Notes:**

- Add enum Stance { ASSAULT, GUARD } in Player.

- Modify calculateDamage() and takeDamage() to factor stance multipliers.

- CLI command: stance assault|guard. Parsing can live in Game.handleInput().

## Weapon Masteries

**Overview:** Weapons are tagged with light‑weight traits (“Slash,” “Pierce,” “Blunt”); landing enough kills with a trait unlocks a passive bonus for that category.

**Player Impact:** Long‑term progression inside combat—players gravitate toward favorite weapon types or deliberately diversify.

**Implementation Notes:**

- Extend Player with Map<WeaponTrait, Integer> kills.
- Define interface WeaponTrait with a single applyBonus(Player p) method for unlocked tiers.
- Update loot tables in EnemyBuilder to include trait‑flagged drops:
```
new EnemyBuilder()
  .withName("Skeleton Guard")
  .withWeapon("Rusty Spear", WeaponTrait.PIERCE)
  .build
```


## Passive Effects 

### Adrenaline Surge

**Overview:** Dropping below a configurable HP percentage boosts the player’s speed and critical chance for a few turns.

**Player Impact:** Transforms low‑health states from pure liability into situational power spikes, rewarding calculated risk.

**Implementation Notes:**

- Add method checkAdrenaline() in Player; trigger when hp < maxHp * 0.25.

- Store adrenalineTurns counter; in calculateHitChance() or getTurnOrder(), multiply by a factor while active.


## Kampfsystem

1 Mechaniken & Vielfalt

| Kategorie             | Kernfunktion                                                                | Erweiterungen                                        |
| --------------------- | --------------------------------------------------------------------------- | ---------------------------------------------------- |
| **Angriff**           | Normal-Hit (Schaden = ATK ± Varianz)                                        | Kritischer Treffer (Crit-Chance, Crit-Multiplikator) |
| **Verteidigung**      | Block (reduziert % Schaden, kosten­frei)                                    | Schild-Fähigkeit (Cooldown; absorbiert x Schaden)    |
| **Ausweichen**        | Ausweich-Wurf gegen Accuracy des Angreifers                                 | *Perfect Dodge* (0 Schaden + Bonus-Initiative)       |
| **Status-Effekte**    | Positiv: Buff (ATK ↑, DEF ↑, Regeneration) <br>Negativ: Gift, Blutung, Stun | Stapelbare Effekte (max Stacks, Dauer pro Stack)     |
| **Sonderfähigkeiten** | Aktive Skills mit Cooldown & Kosten                                         | Flächenangriff, Konter, Team-Synergie-Move           |
| **Reaktionen**        | *On Hit*- oder *On Crit*-Trigger                                            | Auto-Konter, „Rachemodus“ bei HP < x %               |
Du bist ein erfahrener Game-Designer und Java-/XTend-Entwickler mit Schwerpunkt auf rundenbasierten Kampfsystemen. Gemeinsam planen wir ein komplexes, kreatives Kampfmodul für unser bestehendes Java-Spiel (mit XTend), das auf Templates für Räume und Gegner aufbaut.

## Ausgangssituation
- Game-Klassenstruktur liegt vor (Game, Room, Creature, Player, Enemy, Builder-Klassen).
- Aktuell werden Räume und Gegner über Templates erzeugt.

## Ziel
Erstelle ein umfassendes Design für ein rundenbasiertes Kampfsystem, das folgende Anforderungen erfüllt:

1. **Mechaniken & Vielfalt**  
   - Grundlegende Angriffs-, Verteidigungs- und Ausweichmechaniken  
   - Kritische Treffer, Status-Effekte (Stun, Vergiftung, Buffs/Debuffs)  
   - Sonderfähigkeiten mit Abklingzeiten  
   - Kreative Spezialaktionen (z. B. Flächenangriffe, Konter, Team-Aktionen)

2. **Datenstruktur & Klassenentwurf**  
   - Erweiterung der bestehenden Domänen- und Builder-Klassen  
   - Neue Klassen/Interfaces für Fähigkeiten, Effekte und Status  
   - UML-Komponenten- und Sequenzdiagramme für Kampf-Flow

3. **Rundenablauf & Logik**  
   - Initiativsystem basierend auf Agility  
   - Phasen einer Kampf-Runde (Start, Spieleraktion, Gegneraktion, Effekte auswerten, Ende)  
   - Beispielablauf in Pseudocode oder XTend-Syntax

4. **KI-Verhalten**  
   - Einstiegs- bis Profi-Modus: einfache Angriffe bis zu taktischer Planung  
   - Entscheidungsbaum oder Gewichtungssystem für Aktionswahl  
   - Dynamische Anpassung an Spieler-HP und -Ressourcen

5. **Ergebnisformat**  
   - Klare Schritt-für-Schritt-Anleitung  
   - UML-Diagramm-Vorschläge (Component + Sequence)  
   - Code-Beispiele (Pseudocode/XTend) für Kernmethoden  
   - Hinweise zur späteren Integration in bestehende Klassen

Formuliere dein Design so, dass ein Java-/XTend-Team es direkt umsetzen kann.  
