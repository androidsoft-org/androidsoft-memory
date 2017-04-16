PREFIX=animals_

#URL_PREFIX=https://openclipart.org/download
#EXT=svg

# alternative example pattern for png 300px:
URL_PREFIX=https://openclipart.org/image/300px/svg_to_png
EXT=png

DOWNLOAD=if test -e $(EXT)/$(PREFIX)$@; then return 0; fi; mkdir -p $(EXT); /usr/bin/wget -c -O $(EXT)/$(PREFIX)$@ 

OBJS =  pferd schwein chicken biene dragonfly crab butterfly dolphin crow \
	cow jaguar snail fish bear pigeon seahorse kangaroo elephant chipmunk \
	frog antelope starfish swallow grasshopper

.PHONY: png svg link

png: $(addsuffix .png, $(OBJS))
svg: $(addsuffix .svg, $(OBJS))

# Only png can endup inside drawable-mdpi/
link: png
	ln -srf $(addprefix png/$(PREFIX),$(addsuffix .png, $(OBJS))) ../drawable-mdpi/

pferd.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/220784/Pferd-coloured.svg&disposition=attachment"
schwein.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/245556/Schwein-coloured.svg&disposition=attachment"
chicken.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/27980/davidone-Chicken.svg&disposition=attachment"
biene.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/172634/Biene.svg&disposition=attachment"
dragonfly.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/175833/55c89786.svg&disposition=attachment"
crab.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/229601/Crab.svg&disposition=attachment"
butterfly.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/22851/johnny-automatic-butterfly-3.svg&disposition=attachment"
dolphin.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/269678/Simple-Dolphin.svg&disposition=attachment"
crow.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/31303/Crow-1-.svg&disposition=attachment"
cow.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/105259/Holstein-Cow-.svg&disposition=attachment"
jaguar.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/167328/jaguar.svg&disposition=attachment"
snail.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/214781/Schnecke.svg&disposition=attachment"
fish.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/181437/Fish02.svg&disposition=attachment"
bear.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/24590/papapishu-Black-bear.svg&disposition=attachment"
pigeon.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/246804/PigeonHires.svg&disposition=attachment"
seahorse.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/277942/Seahorse3Colour.svg&disposition=attachment"
kangaroo.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/248625/Kangaroo4.svg&disposition=attachment"
elephant.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/249191/Elephant7.svg&disposition=attachment"
chipmunk.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/260451/Chipmunk.svg&disposition=attachment"
frog.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/262108/Frog4.svg&disposition=attachment"
antelope.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/262575/AntelopeClipped.svg&disposition=attachment"
starfish.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/264082/StarFish2.svg&disposition=attachment"
swallow.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/226292/Swallow.svg&disposition=attachment"
grasshopper.$(EXT):
	$(DOWNLOAD) "$(URL_PREFIX)/238809/CommonGreenGrasshopper2.svg&disposition=attachment"
