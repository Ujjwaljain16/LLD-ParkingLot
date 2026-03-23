# Multilevel Parking Lot System

## Class Diagram

```
			 ┌──────────────┐
			 │     Main     │
			 └──────┬───────┘
				│
				▼
		      ┌──────────────────┐
		      │    ParkingLot    │
		      │ +park(...)       │
		      │ +status()        │
		      │ +exit(...)       │
		      └───┬──────┬───────┘
			  │      │
	      ┌───────────┘      └──────────────┐
	      ▼                                  ▼
      ┌───────────────┐                  ┌───────────────┐
      │ ParkingFloor  │                  │     Gate      │
      │ -floorNumber  │                  │ -gateId       │
      │ -slots        │                  │ -floorNumber  │
      └───────┬───────┘                  └───────────────┘
	      │
	      ▼
      ┌───────────────┐         ┌─────────────────┐
      │  ParkingSlot  │◄────────│  ParkingTicket  │
      │ -slotNumber   │         │ -ticketId       │
      │ -slotType     │         │ -vehicle        │
      │ -occupied     │         │ -slotNumber     │
      │ -parkedVehicle│         │ -slotType       │
      └───────┬───────┘         │ -entryTime      │
	      │                 │ -entryGateId    │
	      │                 └─────────────────┘
	      ▼
      ┌───────────────┐
      │    Vehicle    │
      │ -regNumber    │
      │ -vehicleType  │
      └───────────────┘

      ┌─────────────────────────── Interface Layer ───────────────────────────┐
      │                                                                       │
      │  ┌──────────────────────┐   ┌────────────────────────┐                │
      │  │   PricingService     │   │ CompatibilityStrategy  │                │
      │  └──────────┬───────────┘   └──────────┬─────────────┘                │
      │             │                          │                               │
      │             ▼                          ▼                               │
      │  ┌──────────────────────┐   ┌────────────────────────┐                │
      │  │    BillingService    │   │DefaultCompatibilityStr.│                │
      │  └──────────────────────┘   └────────────────────────┘                │
      │                                                                       │
      │  ┌──────────────────────┐   ┌────────────────────────┐                │
      │  │ SlotAssignmentStrat. │   │   TicketIdGenerator    │                │
      │  └──────────┬───────────┘   └──────────┬─────────────┘                │
      │             │                          │                               │
      │             ▼                          ▼                               │
      │  ┌──────────────────────┐   ┌────────────────────────┐                │
      │  │ NearestSlotAssign... │   │ SimpleTicketIdGenerator│                │
      │  └──────────────────────┘   └────────────────────────┘                │
      └───────────────────────────────────────────────────────────────────────┘

Enums:
- VehicleType: BIKE, CAR, BUS
- SlotType: SMALL, MEDIUM, LARGE (with hourly rate)
```

## Why This Approach Was Chosen

1. The problem is state-driven, so in-memory object modeling is enough and clean for LLD.
2. ParkingLot is kept as coordinator and heavy logic is split into small focused classes.
3. Strategy-based slot finding and compatibility keep code flexible without making it complex.
4. Billing is separated so pricing rules can change independently.
5. This gives good SOLID balance while staying simple and student-friendly.

## Explanation

When a vehicle enters:
1. The system validates gate and compatibility.
2. It prepares compatible slot preference based on vehicle type and requested slot.
3. It checks floors by nearest distance from the gate floor.
4. It picks the first free compatible slot.
5. It creates and stores a ticket in active tickets map.

When status is requested:
1. It counts all free slots grouped by slot type.

When a vehicle exits:
1. It validates the ticket from active tickets map.
2. It calculates duration and rounds up to billable hours (minimum 1 hour).
3. It applies slot type hourly rate (not vehicle type).
4. It frees the slot and removes the active ticket.

Compatibility rules used:
- Bike: SMALL, MEDIUM, LARGE
- Car: MEDIUM, LARGE
- Bus: LARGE only

## Quick Start

1. Open terminal in the ParkingLot folder.
2. Compile:

```bash
javac src/*.java
```

3. Run:

```bash
java -cp src Main
```

4. You will see:
- tickets created for parked vehicles
- current availability by slot type
- final bill amounts on exit
---
`LLM was used to polish the content the idea and approch was original `